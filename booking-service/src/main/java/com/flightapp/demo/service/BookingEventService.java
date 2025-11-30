package com.flightapp.demo.service;

import com.flightapp.demo.entity.Booking;

public interface BookingEventService {
	
	void bookingCreated(Booking booking);

	void bookingDeleted(String bookingId);
}
