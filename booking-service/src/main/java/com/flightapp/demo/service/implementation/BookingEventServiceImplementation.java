package com.flightapp.demo.service.implementation;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.BookingEvent;
import com.flightapp.demo.service.BookingEventService;

@Service
public class BookingEventServiceImplementation implements BookingEventService{
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public void bookingCreated(Booking booking) {
        BookingEvent event = new BookingEvent("BOOKING_CREATED", booking);
        kafkaTemplate.send("booking-events", event);
    }

    public void bookingDeleted(String bookingId) {
        BookingEvent event = new BookingEvent("BOOKING_DELETED", bookingId);
        kafkaTemplate.send("booking-events", event);
    }
}

