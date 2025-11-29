package com.flightapp.demo.service.implementation;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.repository.BookingRepository;
import com.flightapp.demo.service.BookingService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingService {
	private final BookingRepository bookingRepo;
//	private final SeatRepository seatRepo;
	private final FlightRepository flightRepo;
//	private final PassengerRepository passengerRepo;
	public ResponseEntity<Booking> getTicketsByPnr(String pnr) {
		return bookingRepo.findByPnr(pnr).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().<Booking>build());
	}

	public ResponseEntity<Booking> getBookingsByEmail(String email) {
		return bookingRepo.findByEmail(email).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().<Booking>build());
		//Sonar cloud maintainability recommendation lambda to reference
	}

	public ResponseEntity<Void> deleteBookingByPnr(String pnr) {
	    return bookingRepo.findByPnr(pnr)
	        .map(booking -> {
	            bookingRepo.delete(booking);  
	            return ResponseEntity.noContent().<Void>build();
	        })
	        .orElse(ResponseEntity.notFound().build());
	}


	public ResponseEntity<Void> bookTicket(String flightId, Booking booking) {
	    return flightRepo.findById(flightId)
	        .map(flight -> {
	            // Fetch seats synchronously
	            List<Seat> seats = seatRepo.findByFlightId(flightId);
	            List<String> seatReq = booking.getSeatNumbers();

	            // Validate requested seats
	            for (String req : seatReq) {
	                Seat seat = seats.stream()
	                    .filter(s -> s.getSeatNumber().equals(req))
	                    .findFirst()
	                    .orElse(null);

	                if (seat == null || !seat.isAvailable()) {
	                    return ResponseEntity.notFound().build();
	                }
	            }

	            // Mark seats unavailable
	            seats.stream()
	                .filter(s -> seatReq.contains(s.getSeatNumber()))
	                .forEach(s -> s.setAvailable(false));

	            // Generate PNR
	            booking.setPnr(flight.getId() + "-" +
	                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));

	            // Calculate price
	            float price = booking.getTripType().equals("ROUND_TRIP")
	                ? flight.getPrice().getRoundTrip()
	                : flight.getPrice().getOneWay();
	            booking.setTotalAmount(price);
	            booking.setFlightId(flightId);

	            // Update available seats
	            flight.setAvailableSeats(flight.getAvailableSeats() - seatReq.size());

	            // Save synchronously
	            flightRepo.save(flight);
	            seatRepo.saveAll(seats);
	            passengerRepo.saveAll(booking.getPassengers());
	            bookingRepo.save(booking);

	            return ResponseEntity.status(HttpStatus.CREATED).build();
	        })
	        .orElse(ResponseEntity.badRequest().build()); // handles missing flight
	}

}

