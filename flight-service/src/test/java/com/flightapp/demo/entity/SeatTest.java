package com.flightapp.demo.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SeatTest {

	@Test
	void testSeatAvailability() {
		Seat seat = new Seat();
		seat.setId("1");
		seat.setSeatNumber("12C");
		seat.setAvailable(true);

		assertEquals("1", seat.getId());
		assertEquals("12C", seat.getSeatNumber());
		assertTrue(seat.isAvailable());

		seat.setAvailable(false);
		assertFalse(seat.isAvailable());
	}
}
