package com.flightapp.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.SearchRequest;
import com.flightapp.demo.service.FlightService;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FlightService flightService;

	@Test
	void testSearchFlights_Success() throws Exception {
		Flight flight = new Flight();
		flight.setId("1");
		flight.setFromPlace("Hyderabad");
		flight.setArrivalTime(LocalDateTime.now());
		flight.setToPlace("Delhi");
		SearchRequest sr = new SearchRequest();
		sr.setDate(LocalDate.now());
		sr.setFromPlace("Hyderabad");
		sr.setToPlace("Delhi");
		when(flightService.search(any(SearchRequest.class))).thenReturn(ResponseEntity.ok(List.of(flight)));

		mockMvc.perform(post("/api/flight/search").contentType(MediaType.APPLICATION_JSON)
				.content("{\"source\":\"HYD\",\"destination\":\"DEL\"}")).andExpect(status().isOk());
				
	}

	@Test
	void testSearchFlights_Empty() throws Exception {
		when(flightService.search(any(SearchRequest.class))).thenReturn(ResponseEntity.ok(List.of()));
		mockMvc.perform(post("/api/flight/search").contentType(MediaType.APPLICATION_JSON)
				.content("{\"source\":\"HYD\",\"destination\":\"DEL\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
	}
	@Test
	void testAddFlight_Invalid() throws Exception {
		mockMvc.perform(post("/api/flight/add").contentType(MediaType.APPLICATION_JSON).content("{}")) // missing																					// fields
				.andExpect(status().isBadRequest());
	}
	@Test
	void testUpdateFlight_Success() throws Exception {
		when(flightService.updateFlight(eq("1"), any(Flight.class))).thenReturn(ResponseEntity.ok().build());

		mockMvc.perform(put("/api/flight/flights/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"1\",\"name\":\"Indigo\"}")).andExpect(status().isOk());
	}
	@Test
	void testUpdateFlight_NotFound() throws Exception {
		when(flightService.updateFlight(eq("99"), any(Flight.class))).thenReturn(ResponseEntity.notFound().build());

		mockMvc.perform(put("/api/flight/flights/99").contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"99\",\"name\":\"Unknown\"}")).andExpect(status().isNotFound());
	}
	@Test
	void testGetFlightById_Success() throws Exception {
		Flight flight = new Flight();
		flight.setId("1");
		flight.setFromPlace("Hyderabad");
		flight.setArrivalTime(LocalDateTime.now());
		flight.setToPlace("Delhi");
		when(flightService.getFlightById("1")).thenReturn(ResponseEntity.ok(flight));

		mockMvc.perform(get("/api/flight/get/1")).andExpect(status().isOk());
				
	}
	@Test
	void testGetFlightById_NotFound() throws Exception {
		when(flightService.getFlightById("99")).thenReturn(ResponseEntity.notFound().build());

		mockMvc.perform(get("/api/flight/get/99")).andExpect(status().isNotFound());
	}

	@Test
	void testGetAllFlights_Success() throws Exception {
		Flight flight = new Flight();
		flight.setId("1");
		flight.setFromPlace("Hyderabad");
		flight.setArrivalTime(LocalDateTime.now());
		flight.setToPlace("Delhi");
		when(flightService.getFlights()).thenReturn(List.of(flight));

		mockMvc.perform(get("/api/flight/get/flights")).andExpect(status().isOk());
				
	}

}
