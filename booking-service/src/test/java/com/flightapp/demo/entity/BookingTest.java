package com.flightapp.demo.entity;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.flightapp.demo.enums.MEAL_PREFERENCE;
import com.flightapp.demo.enums.TRIP_TYPE;

class BookingTest {
	@Test
	void testBookingGettersAndSetters() {
		Booking booking = new Booking();
		booking.setId("1");
		booking.setEmail("hello@example.com");
		booking.setFlightId("2");
		booking.setMealPrefernce(MEAL_PREFERENCE.VEG);
		booking.setTripType(TRIP_TYPE.ONE_WAY);
		booking.setTotalAmount(6000);
		booking.setSeatNumbers(List.of("12C", "12D"));
		Passenger passenger = new Passenger();
		passenger.setId("10");
		passenger.setAge(20);
		passenger.setEmail("hello@example.com");
		passenger.setGender("female");
		assertEquals("1", booking.getId());
		assertEquals("hello@example.com", booking.getEmail());
		assertEquals("2", booking.getFlightId());
		assertEquals(MEAL_PREFERENCE.VEG, booking.getMealPrefernce());
		assertEquals(TRIP_TYPE.ONE_WAY, booking.getTripType());
		assertEquals(6000, booking.getTotalAmount());
	}

	@Test
	void testDefaultValues() {
		Booking booking = new Booking();
		assertNotNull(booking);
		assertNull(booking.getEmail());
		assertNull(booking.getFlightId());
		assertNull(booking.getId());
		assertNull(booking.getMealPrefernce());
		assertNull(booking.getTripType());
		assertEquals(0, booking.getSeatCount());

	}
}
