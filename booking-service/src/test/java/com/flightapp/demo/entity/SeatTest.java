package com.flightapp.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SeatTest {
	@Test
	void teastSeatGettersandSetters() {
		Seat seat = new Seat();
		seat.setId("1");
		seat.setSeatNumber("12C");
		seat.setAvailable(false);
		assertEquals("1", seat.getId());
		assertEquals("12C", seat.getSeatNumber());
		assertFalse(seat.isAvailable());
		seat.setAvailable(true);
		assertTrue(seat.isAvailable());

	}

	@Test
	void testDefaultValues() {
		Seat seat = new Seat();
		assertNotNull(seat);
		assertNull(seat.getId());
		assertNull(seat.getFlightId());
		assertNull(seat.getSeatNumber());

	}
}
