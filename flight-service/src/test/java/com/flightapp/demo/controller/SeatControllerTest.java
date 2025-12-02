package com.flightapp.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.flightapp.demo.entity.Seat;
import com.flightapp.demo.service.SeatService;

@WebMvcTest(SeatController.class)
class SeatControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SeatService seatService;

	@Test
	void testGetSeatsByFlightId_Success() throws Exception {
		Seat seat = new Seat();
		seat.setId("1");
		seat.setAvailable(true);
		when(seatService.getSeatsByFlightId("FL123")).thenReturn(List.of(seat));
		mockMvc.perform(get("/api/seats/flight/FL123")).andExpect(status().isOk());
	}

	@Test
	void testGetSeatsByFlightId_NotFound() throws Exception {
		when(seatService.getSeatsByFlightId("FL999")).thenReturn(Collections.emptyList());
		mockMvc.perform(get("/api/seats/flight/FL999")).andExpect(status().isNotFound());
	}

	@Test
	void testUpdateSeats_Success() throws Exception {
		when(seatService.updateSeats("FL123", List.of())).thenReturn(true);
		mockMvc.perform(put("/api/seats/flights/FL123/seats").contentType(MediaType.APPLICATION_JSON).content("[]"))
				.andExpect(status().isOk());
	}

	@Test
	void testUpdateSeats_NotFound() throws Exception {
		when(seatService.updateSeats("FL999", List.of())).thenReturn(false);
		mockMvc.perform(put("/api/seats/flights/FL999/seats").contentType(MediaType.APPLICATION_JSON).content("[]"))
				.andExpect(status().isNotFound());
	}
}
