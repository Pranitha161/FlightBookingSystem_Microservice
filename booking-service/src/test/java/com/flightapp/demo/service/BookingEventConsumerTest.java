package com.flightapp.demo.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.BookingEvent;
import com.flightapp.demo.service.implementation.BookingEventConsumer;

@ExtendWith(MockitoExtension.class)
class BookingEventConsumerTest {

	@Mock
	private JavaMailSender mailSender;

	@InjectMocks
	private BookingEventConsumer consumer;

	@Test
	void testConsumeSendsEmail() {
		Booking booking = new Booking();
		booking.setEmail("test@example.com");
		booking.setFlightId("FL123");
		BookingEvent event = new BookingEvent();
		event.setType("BOOKING_CREATED");
		event.setBooking(booking);
		consumer.consume(event);
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}

	
}
