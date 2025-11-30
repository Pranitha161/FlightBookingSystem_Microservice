package com.flightapp.demo.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.Price;
import com.flightapp.demo.entity.Seat;
import com.flightapp.demo.enums.TRIP_TYPE;
import com.flightapp.demo.repository.BookingRepository;
import com.flightapp.demo.repository.PassengerRepository;

import com.flightapp.demo.service.implementation.BookingServiceImplementation;
import com.flightapp.demo.feign.FlightClient;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepo;
    @Mock
    private PassengerRepository passengerRepo;
    @Mock
    private FlightClient flightClient;
    @Mock
    private BookingEventService eventProducer;

    @InjectMocks
    private BookingServiceImplementation bookingService;

    @Test
    void testGetTicketsByPnrSuccess() {
        Booking booking = new Booking();
        booking.setPnr("PNR123");
        when(bookingRepo.findByPnr("PNR123")).thenReturn(Optional.of(booking));
        ResponseEntity<Booking> response = bookingService.getTicketsByPnr("PNR123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPnr()).isEqualTo("PNR123");
    }

    @Test
    void testGetTicketsByPnrNotFound() {
        when(bookingRepo.findByPnr("PNR999")).thenReturn(Optional.empty());
        ResponseEntity<Booking> response = bookingService.getTicketsByPnr("PNR999");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetBookingsByEmailSuccess() {
        Booking booking = new Booking();
        booking.setEmail("john@example.com");
        when(bookingRepo.findByEmail("john@example.com")).thenReturn(Optional.of(booking));

        ResponseEntity<Booking> response = bookingService.getBookingsByEmail("john@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testGetBookingsByEmailNotFound() {
        when(bookingRepo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        ResponseEntity<Booking> response = bookingService.getBookingsByEmail("unknown@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteBookingSuccess() {
        Booking booking = new Booking();
        booking.setPnr("PNR123");
        booking.setFlightId("FL123");
        booking.setSeatNumbers(Arrays.asList("1A"));

        Flight flight = new Flight();
        flight.setId("FL123");
        flight.setAvailableSeats(10);

        Seat seat = new Seat();
        seat.setSeatNumber("1A");
        seat.setAvailable(false);

        when(bookingRepo.findByPnr("PNR123")).thenReturn(Optional.of(booking));
        when(flightClient.getFlightById("FL123")).thenReturn(ResponseEntity.ok(flight));
        when(flightClient.getSeatsByFlightId("FL123")).thenReturn(ResponseEntity.ok(Arrays.asList(seat)));

        ResponseEntity<String> response = bookingService.deleteBookingByPnr("PNR123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted successfully");
        verify(bookingRepo).delete(booking);
        verify(eventProducer).bookingDeleted(booking.getId());
    }

    @Test
    void testDeleteBookingNotFound() {
        when(bookingRepo.findByPnr("PNR999")).thenReturn(Optional.empty());

        ResponseEntity<String> response = bookingService.deleteBookingByPnr("PNR999");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Booking not found");
    }

    @Test
    void testBookTicketSuccess() {
        Booking booking = new Booking();
        booking.setSeatNumbers(Arrays.asList("1A"));
        booking.setTripType(TRIP_TYPE.ONE_WAY);

        Flight flight = new Flight();
        flight.setId("FL123");
        flight.setAvailableSeats(10);
        Price p=new Price();
        p.setOneWay(100f);
        p.setRoundTrip(200f);
        flight.setPrice(p);

        Seat seat = new Seat();
        seat.setSeatNumber("1A");
        seat.setAvailable(true);

        when(flightClient.getFlightById("FL123")).thenReturn(ResponseEntity.ok(flight));
        when(flightClient.getSeatsByFlightId("FL123")).thenReturn(ResponseEntity.ok(Arrays.asList(seat)));

        ResponseEntity<String> response = bookingService.bookTicket("FL123", booking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Booking created successfully");
        verify(bookingRepo).save(booking);
        verify(eventProducer).bookingCreated(booking);
    }

    @Test
    void testBookTicketNoSeatsRequested() {
        Booking booking = new Booking();
        booking.setSeatNumbers(Collections.emptyList());

        Flight flight = new Flight();
        flight.setId("FL123");
        Price p=new Price();
        p.setOneWay(100f);
        p.setRoundTrip(200f);
        flight.setPrice(p);

        when(flightClient.getFlightById("FL123")).thenReturn(ResponseEntity.ok(flight));
        when(flightClient.getSeatsByFlightId("FL123")).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        ResponseEntity<String> response = bookingService.bookTicket("FL123", booking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("No seats requested");
    }

    @Test
    void testBookTicketSeatNotAvailable() {
        Booking booking = new Booking();
        booking.setSeatNumbers(Arrays.asList("1A"));

        Flight flight = new Flight();
        flight.setId("FL123");
        Price p=new Price();
        p.setOneWay(100f);
        p.setRoundTrip(200f);
        flight.setPrice(p);

        Seat seat = new Seat();
        seat.setSeatNumber("1A");
        seat.setAvailable(false);

        when(flightClient.getFlightById("FL123")).thenReturn(ResponseEntity.ok(flight));
        when(flightClient.getSeatsByFlightId("FL123")).thenReturn(ResponseEntity.ok(Arrays.asList(seat)));

        ResponseEntity<String> response = bookingService.bookTicket("FL123", booking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Seat 1A is not available");
    }

    @Test
    void testBookTicketInvalidPassengerIds() {
        Booking booking = new Booking();
        booking.setSeatNumbers(Arrays.asList("1A"));
        booking.setPassengerIds(Arrays.asList("P001"));
        booking.setTripType(TRIP_TYPE.ONE_WAY);

        Flight flight = new Flight();
        flight.setId("FL123");
        Price p=new Price();
        p.setOneWay(100f);
        p.setRoundTrip(200f);
        flight.setPrice(p);

        Seat seat = new Seat();
        seat.setSeatNumber("1A");
        seat.setAvailable(true);

        when(flightClient.getFlightById("FL123")).thenReturn(ResponseEntity.ok(flight));
        when(flightClient.getSeatsByFlightId("FL123")).thenReturn(ResponseEntity.ok(Arrays.asList(seat)));
        when(passengerRepo.findAllById(Arrays.asList("P001"))).thenReturn(Collections.emptyList());

        ResponseEntity<String> response = bookingService.bookTicket("FL123", booking);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).contains("Passenger IDs invalid");
    }
}
