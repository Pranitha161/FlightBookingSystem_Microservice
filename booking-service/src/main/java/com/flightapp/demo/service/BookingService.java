package com.flightapp.demo.service;

import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Booking;

public interface BookingService {
	ResponseEntity<String> bookTicket(String flightId, Booking booking);

	ResponseEntity<Booking> getTicketsByPnr(String pnr);

	ResponseEntity<Booking> getBookingsByEmail(String email);

	ResponseEntity<String> deleteBookingByPnr(String pnr);


}
