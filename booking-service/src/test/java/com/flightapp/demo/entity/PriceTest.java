package com.flightapp.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class PriceTest {
	@Test
	void testPriceTestGettersAndSetters() {
		Price price = new Price();
		price.setOneWay(1000);
		price.setRoundTrip(100000);
		assertEquals(1000, price.getOneWay());
		assertEquals(100000, price.getRoundTrip());
	}

	@Test
	void testDefaultValues() {
		Price price = new Price();
		assertNotNull(price);
		assertEquals(0, price.getOneWay());
		assertEquals(0, price.getRoundTrip());

	}
}
