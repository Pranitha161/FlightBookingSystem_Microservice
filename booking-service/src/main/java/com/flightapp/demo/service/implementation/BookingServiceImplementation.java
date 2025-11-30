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
import com.flightapp.demo.feign.FlightClient;
import com.flightapp.demo.repository.BookingRepository;
import com.flightapp.demo.repository.PassengerRepository;
import com.flightapp.demo.service.BookingService;

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

	public ResponseEntity<Void> deleteBookingByPnr(String pnr) {
		return bookingRepo.findByPnr(pnr).map(booking -> {
			bookingRepo.delete(booking);
			return ResponseEntity.noContent().<Void>build();
		}).orElse(ResponseEntity.notFound().build());
	}

	public ResponseEntity<Void> bookTicket(String flightId, Booking booking) {
		ResponseEntity<Flight> flightResponse = flightClient.getFlightById(flightId);
		if (!flightResponse.getStatusCode().is2xxSuccessful() || flightResponse.getBody() == null) {
			return ResponseEntity.badRequest().build();
		}
		Flight flight = flightResponse.getBody();
		List<Seat> seats = flightClient.getSeatsByFlightId(flightId).getBody();
		List<String> seatReq = booking.getSeatNumbers();
		for (String req : seatReq) {
			Seat seat = seats.stream().filter(s -> s.getSeatNumber().equals(req)).findFirst().orElse(null);
			if (seat == null || !seat.isAvailable()) {
				return ResponseEntity.notFound().build();
			}
		}
		seats.stream().filter(s -> seatReq.contains(s.getSeatNumber())).forEach(s -> s.setAvailable(false));
		booking.setPnr(flight.getId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));
		float price = booking.getTripType().equals("ROUND_TRIP") ? flight.getPrice().getRoundTrip()
				: flight.getPrice().getOneWay();
		booking.setTotalAmount(price);
		booking.setFlightId(flightId);

		flight.setAvailableSeats(flight.getAvailableSeats() - seatReq.size());
		flightClient.updateFlight(flightId, flight);

		flightClient.updateSeats(flightId, seats);

		List<Passenger> passengers = passengerRepo.findAllById(booking.getPassengerIds());
		passengerRepo.saveAll(passengers);

		bookingRepo.save(booking);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
