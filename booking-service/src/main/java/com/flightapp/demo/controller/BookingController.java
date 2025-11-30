package com.flightapp.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookingController {
	private final BookingService bookingService;

	@PostMapping("booking/{flightId}")
	public ResponseEntity<String> bookTicket(@RequestBody Booking booking, @PathVariable String flightId) {
		return bookingService.bookTicket(flightId, booking);
	}

	@GetMapping("ticket/{pnr}")
	public ResponseEntity<Booking> getByPnr(@PathVariable String pnr) {
		return bookingService.getTicketsByPnr(pnr);
	}

	@GetMapping("/history/{emailId}")
	public ResponseEntity<Booking> getByemailId(@PathVariable String emailId) {
		return bookingService.getBookingsByEmail(emailId);
	}

	@DeleteMapping("/booking/cancel/{pnr}")
	public ResponseEntity<String> cancelBooking(@PathVariable String pnr) {
		return bookingService.deleteBookingByPnr(pnr);
	}
}
