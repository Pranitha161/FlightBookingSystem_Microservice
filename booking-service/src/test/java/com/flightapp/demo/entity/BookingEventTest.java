package com.flightapp.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class BookingEventTest {
	@Test
	void teastSeatGettersandSetters() {
		BookingEvent booking = new BookingEvent();
		booking.setType("BOOKED");
		booking.setBookingId("123");
		assertEquals("BOOKED", booking.getType());
	}

	@Test
	void testDefaultValues() {
		BookingEvent booking = new BookingEvent();
		assertNotNull(booking);
		assertNull(booking.getBooking());
		assertNull(booking.getType());

	}
	@Test
	void testWithBookinId() {
		BookingEvent booking = new BookingEvent();
		booking.setBookingId("1");
		assertNotNull(booking);
		assertEquals("1",booking.getBookingId());
		assertNull(booking.getType());

	}
}
