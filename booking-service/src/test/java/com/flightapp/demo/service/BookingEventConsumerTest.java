package com.flightapp.demo.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.BookingEvent;
import com.flightapp.demo.service.implementation.BookingEventConsumer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingEventConsumerTest {

	@Mock
	private JavaMailSender mailSender;

	@InjectMocks
	private BookingEventConsumer consumer;

	private BookingEvent buildEvent() {
		Booking booking = new Booking();
		booking.setEmail("test@example.com");
		booking.setFlightId("FL123");
		BookingEvent event = new BookingEvent();
		event.setType("BOOKING_CREATED");
		event.setBooking(booking);
		return event;
	}

	@Test
	void testConsumeSendsEmailSuccessfully() {
		BookingEvent event = buildEvent();
		consumer.consume(event);
		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mailSender, times(1)).send(captor.capture());

		SimpleMailMessage sentMessage = captor.getValue();
		assertEquals("test@example.com", sentMessage.getTo()[0]);
		assertEquals("Booking Update: BOOKING_CREATED", sentMessage.getSubject());
		assertTrue(sentMessage.getText().contains("flight FL123"));
	}

	@Test
	void testConsumeHandlesExceptionGracefully() {
		BookingEvent event = buildEvent();
		doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));
		assertDoesNotThrow(() -> consumer.consume(event));
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
}
