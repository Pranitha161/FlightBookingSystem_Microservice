package com.flightapp.demo.service.implementation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.Passenger;
import com.flightapp.demo.entity.Seat;
import com.flightapp.demo.enums.TRIP_TYPE;
import com.flightapp.demo.feign.FlightClient;
import com.flightapp.demo.repository.BookingRepository;
import com.flightapp.demo.repository.PassengerRepository;
import com.flightapp.demo.service.BookingEventService;
import com.flightapp.demo.service.BookingService;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingService {
	private final BookingRepository bookingRepo;
	private final FlightClient flightClient;
	private final PassengerRepository passengerRepo;
	private final BookingEventService eventProducer;

	public ResponseEntity<Booking> getTicketsByPnr(String pnr) {
		return bookingRepo.findByPnr(pnr).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().<Booking>build());
	}

	public ResponseEntity<Booking> getBookingsByEmail(String email) {
		return bookingRepo.findByEmail(email).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().<Booking>build());

	}

	@CircuitBreaker(name = "flightServiceCircuitBreaker", fallbackMethod = "fallbackDeleteBooking")
	public ResponseEntity<String> deleteBookingByPnr(String pnr) {
		return bookingRepo.findByPnr(pnr).map(booking -> {
			ResponseEntity<Flight> flightResponse = flightClient.getFlightById(booking.getFlightId());
			if (!flightResponse.getStatusCode().is2xxSuccessful() || flightResponse.getBody() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found for booking PNR: " + pnr);
			}
			Flight flight = flightResponse.getBody();
			ZoneId systemZone = ZoneId.systemDefault();
			ZonedDateTime now = ZonedDateTime.now(systemZone);
			ZonedDateTime departure = flight.getDepartureTime().atZone(systemZone);
			if (departure.isBefore(now.plusHours(24))) {
			    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			        .body("Cannot delete booking within 24 hours of departure for PNR: " + pnr);
			}

			ResponseEntity<List<Seat>> seatResponse = flightClient.getSeatsByFlightId(booking.getFlightId());
			if (!seatResponse.getStatusCode().is2xxSuccessful() || seatResponse.getBody() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Seats not found for flight: " + booking.getFlightId());
			}
			List<Seat> seats = seatResponse.getBody();
			List<String> seatNumbers = booking.getSeatNumbers();
			seats.stream().filter(s -> seatNumbers.contains(s.getSeatNumber())).forEach(s -> s.setAvailable(true));
			flight.setAvailableSeats(flight.getAvailableSeats() + seatNumbers.size());
			flightClient.updateFlight(flight.getId(), flight);
			flightClient.updateSeats(flight.getId(), seats);
			bookingRepo.delete(booking);
			eventProducer.bookingDeleted(booking);
			return ResponseEntity.ok("Booking with PNR " + pnr + " deleted successfully. Seats released.");
		}).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found"));
	}

	public ResponseEntity<String> fallbackDeleteBooking(String pnr, Throwable t) {
		if (t instanceof feign.FeignException.NotFound) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Flight not found while deleting booking with PNR: " + pnr);
		}
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Flight service unavailable while deleting booking with PNR: " + pnr);
	}

	@Transactional
	@CircuitBreaker(name = "flightServiceCircuitBreaker", fallbackMethod = "fallbackGetFlight")
	public ResponseEntity<String> bookTicket(String flightId, Booking booking) {
		final List<String> seatReq = booking.getSeatNumbers();
		if (seatReq == null || seatReq.isEmpty()) {
			return ResponseEntity.badRequest().body("No seats requested");
		}
		ResponseEntity<Flight> flightResp = flightClient.getFlightById(flightId);
		if (!flightResp.getStatusCode().is2xxSuccessful() || flightResp.getBody() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found with id: " + flightId);
		}
		Flight flight = flightResp.getBody();

		ResponseEntity<List<Seat>> seatsResp = flightClient.getSeatsByFlightId(flightId);
		if (!seatsResp.getStatusCode().is2xxSuccessful() || seatsResp.getBody() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seats not found for flight: " + flightId);
		}
		List<Seat> seats = seatsResp.getBody();
		for (String req : seatReq) {
			Seat seat = seats.stream().filter(s -> req.equals(s.getSeatNumber())).findFirst().orElse(null);
			if (seat == null) {
				return ResponseEntity.badRequest().body("Seat " + req + " does not exist");
			}
			if (!seat.isAvailable()) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Seat " + req + " is already booked");
			}
		}
		booking.setPnr(flight.getId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));
		float price = booking.getTripType() == TRIP_TYPE.ROUND_TRIP ? flight.getPrice().getRoundTrip()
				: flight.getPrice().getOneWay();
		booking.setTotalAmount(price);
		booking.setFlightId(flightId);
		
		if (booking.getPassengerIds() != null && !booking.getPassengerIds().isEmpty()) {
			List<Passenger> passengers = passengerRepo.findAllById(booking.getPassengerIds());
			if (passengers.isEmpty()) {
				return ResponseEntity.unprocessableEntity().body("Passenger IDs invalid");
			}
			passengerRepo.saveAll(passengers);
		}
		seats.stream().filter(s -> seatReq.contains(s.getSeatNumber())).forEach(s -> s.setAvailable(false));

		int newAvailable = flight.getAvailableSeats() - seatReq.size();
		if (newAvailable < 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Insufficient seats available");
		}
		flight.setAvailableSeats(newAvailable);
		bookingRepo.save(booking);
		flightClient.updateFlight(flightId, flight);
		flightClient.updateSeats(flightId, seats);
		eventProducer.bookingCreated(booking);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Booking created successfully with PNR: " + booking.getPnr());
	}

	public ResponseEntity<String> fallbackGetFlight(String flightId, Booking booking, Throwable t) {
		System.out.println(t.getMessage());
		if (t instanceof FeignException.NotFound) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Flight not found with id: " + flightId);
	    } else if (t instanceof FeignException.ServiceUnavailable) {
	        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	                .body("Flight service unavailable while booking flightId: " + flightId);
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Unexpected error while booking flightId: " + flightId);
	    }
	}

}
