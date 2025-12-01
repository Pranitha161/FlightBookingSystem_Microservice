package com.flightapp.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
}
