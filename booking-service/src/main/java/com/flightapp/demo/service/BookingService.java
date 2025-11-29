package com.flightapp.demo.service;

import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Booking;

public interface BookingService {
	ResponseEntity<Void> bookTicket(String flightId, Booking booking);

	ResponseEntity<Booking> getTicketsByPnr(String pnr);

	ResponseEntity<Booking> getBookingsByEmail(String email);

	ResponseEntity<Void> deleteBookingByPnr(String pnr);


}
