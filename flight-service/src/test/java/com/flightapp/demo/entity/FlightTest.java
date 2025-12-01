package com.flightapp.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class FlightTest {
	@Test
	void testFlightGettersAndSetters() {
		Flight flight = new Flight();
		flight.setId("1");
		flight.setFromPlace("Hyderabad");
		flight.setToPlace("Mumbai");
		flight.setArrivalTime(LocalDateTime.of(2025, 11, 20, 10, 30));
		flight.setDepartureTime(LocalDateTime.of(2025, 12, 20, 10, 30));
		Price price = new Price();
		price.setOneWay(2000);
		price.setRoundTrip(9000);
		flight.setPrice(price);
		assertEquals("1", flight.getId());
		assertEquals("Hyderabad", flight.getFromPlace());
		assertEquals("Mumbai", flight.getToPlace());
		assertEquals(LocalDateTime.of(2025, 11, 20, 10, 30), flight.getArrivalTime());
		assertEquals(LocalDateTime.of(2025, 12, 20, 10, 30), flight.getDepartureTime());
		assertEquals(2000, flight.getPrice().getOneWay());
		assertEquals(9000, flight.getPrice().getRoundTrip());
	}

	@Test
	void testDefaultValues() {
		Flight flight = new Flight();
		assertNotNull(flight);
		assertNull(flight.getId());
		assertNull(flight.getAirlineId());
		assertNull(flight.getArrivalTime());
		assertNull(flight.getDepartureTime());
	}
}


