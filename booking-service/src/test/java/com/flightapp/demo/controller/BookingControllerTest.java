package com.flightapp.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.flightapp.demo.entity.Booking;
import com.flightapp.demo.service.BookingService;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void testBookTicketSuccess() throws Exception {
        Booking booking = new Booking();
        booking.setPnr("PNR123");
        booking.setFlightId("FL123");

        when(bookingService.bookTicket("FL123", booking))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED)
                                      .body("Booking created successfully with PNR: PNR123"));

        mockMvc.perform(post("/api/booking/FL123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pnr\":\"PNR123\",\"flightId\":\"FL123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Booking created successfully with PNR: PNR123"));
    }
    @Test
    void testBookTicketNoSeatsRequested() throws Exception {
        when(bookingService.bookTicket("FL123", new Booking()))
            .thenReturn(ResponseEntity.badRequest().body("No seats requested"));

        mockMvc.perform(post("/api/booking/FL123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pnr\":\"PNR123\",\"flightId\":\"FL123\"}"));
    }

    @Test
    void testGetByPnrSuccess() throws Exception {
        Booking booking = new Booking();
        booking.setPnr("123");
        booking.setFlightId("12");
        when(bookingService.getTicketsByPnr("123"))
            .thenReturn(ResponseEntity.ok(booking));
        mockMvc.perform(get("/api/ticket/123"))
               .andExpect(status().isOk());
             
    }

    @Test
    void testGetByEmailSuccess() throws Exception {
        Booking booking = new Booking();
        booking.setPnr("PNR456");
        booking.setFlightId("FL456");

        when(bookingService.getBookingsByEmail("john@example.com"))
            .thenReturn(ResponseEntity.ok(booking));

        mockMvc.perform(get("/api/history/john@example.com"))
               .andExpect(status().isOk());
               
    }

    @Test
    void testCancelBookingSuccess() throws Exception {
        when(bookingService.deleteBookingByPnr("PNR123"))
            .thenReturn(ResponseEntity.ok("Booking cancelled successfully"));

        mockMvc.perform(delete("/api/booking/cancel/PNR123"))
               .andExpect(status().isOk());
            
    }

 

    @Test
    void testGetByPnrNotFound() throws Exception {
        when(bookingService.getTicketsByPnr("999"))
            .thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/ticket/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testGetByEmailNotFound() throws Exception {
        when(bookingService.getBookingsByEmail("unknown@example.com"))
            .thenReturn(ResponseEntity.notFound().build());
        mockMvc.perform(get("/api/history/unknown@example.com"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testCancelBookingNotFound() throws Exception {
        when(bookingService.deleteBookingByPnr("PNR999"))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));                          
        mockMvc.perform(delete("/api/booking/cancel/PNR999"))
               .andExpect(status().isNotFound());
               
    }
}
