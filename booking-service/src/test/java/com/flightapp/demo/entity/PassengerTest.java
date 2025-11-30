package com.flightapp.demo.entity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class PassengerTest {
	@Test
	void testPassengerGettersandSetter() {
		Passenger passenger = new Passenger();
		passenger.setId("1");
		passenger.setName("Hello");
		passenger.setAge(10);
		passenger.setEmail("hello@example.com");
		passenger.setBookingId("12");
		assertEquals("1", passenger.getId());
		assertEquals("Hello", passenger.getName());
		assertEquals(10, passenger.getAge());
		assertEquals("hello@example.com", passenger.getEmail());
		assertNotNull(passenger);
	}

	@Test
	void testDefaultValues() {
		Passenger passenger = new Passenger();
		assertNotNull(passenger);
		assertEquals(0, passenger.getAge());
		assertNull(passenger.getBookingId());
		assertNull(passenger.getEmail());
		assertNull(passenger.getGender());
		assertNull(passenger.getId());

	}
}

