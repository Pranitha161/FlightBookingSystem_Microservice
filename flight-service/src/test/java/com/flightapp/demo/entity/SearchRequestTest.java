package com.flightapp.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class SearchRequestTest {
	@Test
	void TestSearchRequestGettersAndSetters() {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setToPlace("Hyderabad");
		searchRequest.setFromPlace("Mumbai");
		searchRequest.setDate(LocalDate.of(2025, 11, 20));
		assertEquals("Hyderabad", searchRequest.getToPlace());
		assertEquals("Mumbai", searchRequest.getFromPlace());
		assertEquals(LocalDate.of(2025, 11, 20), searchRequest.getDate());
	}

	@Test
	void testDefaultValues() {
		SearchRequest searchRequest = new SearchRequest();
		assertNotNull(searchRequest);
		assertNull(searchRequest.getFromPlace());
		assertNull(searchRequest.getToPlace());
		assertNull(searchRequest.getDate());
	}
}
