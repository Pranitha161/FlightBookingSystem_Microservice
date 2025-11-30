package com.flightapp.demo.service.implementation;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.BookingEvent;

@Service
public class BookingEventConsumer {

    private final JavaMailSender mailSender;
    
    public BookingEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "booking-events", groupId = "booking-group")
    public void consume(BookingEvent event) {
        if (event.getPayload() instanceof Booking booking) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getEmail());  
            message.setSubject("Booking Update: " + event.getType());
            message.setText("Hello, Your booking for flight " + booking.getFlightId() +
                            " has been " + (event.getType().equals("BOOKING_CREATED") ? "confirmed." : "cancelled.") +
                            "\n\nThank you for choosing us!");
            mailSender.send(message);

            System.out.println("Email sent to passenger: " + booking.getEmail());
        }
    }
}
