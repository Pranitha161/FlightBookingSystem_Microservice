package com.flightapp.demo.service.implementation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.Passenger;
import com.flightapp.demo.entity.Seat;
import com.flightapp.demo.enums.TRIP_TYPE;
import com.flightapp.demo.feign.FlightClient;
import com.flightapp.demo.repository.BookingRepository;
import com.flightapp.demo.repository.PassengerRepository;
import com.flightapp.demo.service.BookingService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingService {
	private final BookingRepository bookingRepo;
	private final FlightClient flightClient;
	private final PassengerRepository passengerRepo;

	public ResponseEntity<Booking> getTicketsByPnr(String pnr) {
		return bookingRepo.findByPnr(pnr).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().<Booking>build());
	}

	public ResponseEntity<Booking> getBookingsByEmail(String email) {
		return bookingRepo.findByEmail(email).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().<Booking>build());
		// Sonar cloud maintainability recommendation lambda to reference
	}

	public ResponseEntity<String> deleteBookingByPnr(String pnr) {
		return bookingRepo.findByPnr(pnr).map(booking -> {
			try {
				ResponseEntity<Flight> flightResponse = flightClient.getFlightById(booking.getFlightId());
				if (!flightResponse.getStatusCode().is2xxSuccessful() || flightResponse.getBody() == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found for booking PNR: " + pnr);
				}
				Flight flight = flightResponse.getBody();
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

				return ResponseEntity.status(HttpStatus.OK)
						.body("Booking with PNR " + pnr + " deleted successfully. Seats released.");
			} catch (FeignException e) {
				return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
						.body("Failed to update flight or seats while deleting booking.");
			}
		}).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found "));
	}

	public ResponseEntity<String> bookTicket(String flightId, Booking booking) {
		Flight flight;
		List<Seat> seats;
		try {
			ResponseEntity<Flight> flightResponse = flightClient.getFlightById(flightId);
			if (!flightResponse.getStatusCode().is2xxSuccessful() || flightResponse.getBody() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found with id: " + flightId);
			}
			flight = flightResponse.getBody();
		} catch (FeignException.NotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found with id: " + flightId);
		} catch (FeignException e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Flight service unavailable");
		}

		try {
			ResponseEntity<List<Seat>> seatResponse = flightClient.getSeatsByFlightId(flightId);
			if (!seatResponse.getStatusCode().is2xxSuccessful() || seatResponse.getBody() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seats not found for flight: " + flightId);
			}
			seats = seatResponse.getBody();
		} catch (FeignException.NotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seats not found for flight: " + flightId);
		} catch (FeignException e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Seat service unavailable");
		}

		List<String> seatReq = booking.getSeatNumbers();
		if (seatReq == null || seatReq.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No seats requested");
		}
		for (String req : seatReq) {
			Seat seat = seats.stream().filter(s -> s.getSeatNumber().equals(req)).findFirst().orElse(null);
			if (seat == null || !seat.isAvailable()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seat " + req + " is not available");
			}
		}

		seats.stream().filter(s -> seatReq.contains(s.getSeatNumber())).forEach(s -> s.setAvailable(false));

		booking.setPnr(flight.getId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));

		float price = booking.getTripType() == TRIP_TYPE.ROUND_TRIP ? flight.getPrice().getRoundTrip()
				: flight.getPrice().getOneWay();
		booking.setTotalAmount(price);
		booking.setFlightId(flightId);

		try {
			flight.setAvailableSeats(flight.getAvailableSeats() - seatReq.size());
			flightClient.updateFlight(flightId, flight);
		} catch (FeignException e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Failed to update flight seats");
		}

		try {
			flightClient.updateSeats(flightId, seats);
		} catch (FeignException e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Failed to update seat availability");
		}

		if (booking.getPassengerIds() != null && !booking.getPassengerIds().isEmpty()) {
			List<Passenger> passengers = passengerRepo.findAllById(booking.getPassengerIds());
			if (passengers.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Passenger IDs invalid");
			}
			passengerRepo.saveAll(passengers);
		}

		bookingRepo.save(booking);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Booking created successfully with PNR: " + booking.getPnr());
	}

}
