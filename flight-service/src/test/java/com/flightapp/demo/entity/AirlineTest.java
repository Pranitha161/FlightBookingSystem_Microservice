package com.flightapp.demo.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AirlineTest {

	@Test
	void testAirlineGettersAndSetters() {
		Airline airline = new Airline();
		airline.setId("1");
		airline.setName("Indigo");
		airline.setLogoUrl("http://logo.url");
		Flight flight1 = new Flight();
		flight1.setId("10");
		flight1.setFromPlace("Delhi");
		flight1.setToPlace("Hyderabad");
		Flight flight2 = new Flight();
		flight2.setId("20");
		flight2.setFromPlace("Mumbai");
		flight2.setToPlace("Chennai");
		assertEquals("1", airline.getId());
		assertEquals("Indigo", airline.getName());
		assertEquals("http://logo.url", airline.getLogoUrl());

	}
	@Test
	void testDefaultValues() {
		Airline airline = new Airline();
		assertNotNull(airline);
		assertNull(airline.getId());
	}
}
