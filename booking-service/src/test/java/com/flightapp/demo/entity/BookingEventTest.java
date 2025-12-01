package com.flightapp.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class BookingEventTest {
	@Test
	void teastSeatGettersandSetters() {
		BookingEvent booking = new BookingEvent();
		booking.setType("BOOKED");
		assertEquals("BOOKED", booking.getType());
	}

	@Test
	void testDefaultValues() {
		BookingEvent booking = new BookingEvent();
		assertNotNull(booking);
//		assertNull(booking.getPayload());
		assertNull(booking.getType());

	}
}
