package com.flightapp.demo.service.implementation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.flightapp.demo.config.RabbitConfig;
import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.BookingEvent;

@Service
public class BookingEventConsumer {

    private final JavaMailSender mailSender;

    public BookingEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void consume(BookingEvent event) {
        try {
            Booking booking = event.getBooking();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getEmail());
            message.setSubject("Booking Update: " + event.getType());
            message.setText("Hello, Your booking for flight " + booking.getFlightId() + " has been confirmed.");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error in consumer: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
