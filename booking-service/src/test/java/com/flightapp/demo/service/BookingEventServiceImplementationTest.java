package com.flightapp.demo.service;



import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flightapp.demo.config.RabbitConfig;
import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.BookingEvent;
import com.flightapp.demo.service.implementation.BookingEventServiceImplementation;

@ExtendWith(MockitoExtension.class)
class BookingEventServiceImplementationTest {

    @Mock
    private org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @InjectMocks
    private BookingEventServiceImplementation service;

    @Test
    void testBookingCreatedPublishesEvent() {
        Booking booking = new Booking();
        booking.setPnr("PNR123");

        service.bookingCreated(booking);

        // Capture the event sent to RabbitTemplate
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitConfig.EXCHANGE), eq(RabbitConfig.ROUTING_KEY), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(BookingEvent.class);
        BookingEvent event = (BookingEvent) captor.getValue();
        assertThat(event.getType()).isEqualTo("BOOKING_CREATED");
        assertThat(event.getBooking().getPnr()).isEqualTo("PNR123");
    }

    @Test
    void testBookingDeletedPublishesEvent() {
        Booking booking = new Booking();
        booking.setPnr("PNR999");

        service.bookingDeleted(booking);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitConfig.EXCHANGE), eq(RabbitConfig.ROUTING_KEY), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(BookingEvent.class);
        BookingEvent event = (BookingEvent) captor.getValue();
        assertThat(event.getType()).isEqualTo("BOOKING_DELETED");
        assertThat(event.getBooking().getPnr()).isEqualTo("PNR999");
    }
}

