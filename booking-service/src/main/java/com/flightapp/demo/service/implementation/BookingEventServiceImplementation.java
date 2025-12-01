package com.flightapp.demo.service.implementation;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.flightapp.demo.config.RabbitConfig;
import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.BookingEvent;
import com.flightapp.demo.service.BookingEventService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingEventServiceImplementation implements BookingEventService {

    private final RabbitTemplate rabbitTemplate;

    public void bookingCreated(Booking booking) {
        BookingEvent event = new BookingEvent("BOOKING_CREATED", booking);
        System.out.println("Publishing event: " + event);
        rabbitTemplate.convertAndSend(
            RabbitConfig.EXCHANGE,
            RabbitConfig.ROUTING_KEY,
            event
        );
    }

    public void bookingDeleted(Booking booking) {
        BookingEvent event = new BookingEvent("BOOKING_DELETED", booking);
        rabbitTemplate.convertAndSend(
            RabbitConfig.EXCHANGE,
            RabbitConfig.ROUTING_KEY,
            event
        );
    }
}
