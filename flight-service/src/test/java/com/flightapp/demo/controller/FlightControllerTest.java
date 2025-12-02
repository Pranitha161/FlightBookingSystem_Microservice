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
	void testSearchFlightReturnsFlights() throws Exception {
		Flight flight = new Flight();
		flight.setId("FL123");

		when(flightService.search(any(SearchRequest.class))).thenReturn(ResponseEntity.ok(List.of(flight)));

		mockMvc.perform(post("/api/flight/search").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromPlace\":\"HYD\",\"toPlace\":\"DEL\",\"date\":\"2025-12-15\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("FL123"));
	}

	@Test
	void testSearchFlightNoContent() throws Exception {
		when(flightService.search(any(SearchRequest.class))).thenReturn(ResponseEntity.noContent().build());

		mockMvc.perform(post("/api/flight/search").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromPlace\":\"HYD\",\"toPlace\":\"DEL\",\"date\":\"2025-12-15\"}"))
				.andExpect(status().isNoContent());
	}

	@Test
	void testAddFlight_Invalid() throws Exception {
		mockMvc.perform(post("/api/flight/add").contentType(MediaType.APPLICATION_JSON).content("{}")) // missing //																						// fields
				.andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateFlightEndpointSuccess() throws Exception {
	    when(flightService.updateFlight(eq("FL123"), any(Flight.class)))
	        .thenReturn(ResponseEntity.ok().build());

	    mockMvc.perform(put("/api/flight/flights/FL123")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"fromPlace\":\"HYD\",\"toPlace\":\"DEL\",\"airlineId\":\"AI001\","
	                   + "\"arrivalTime\":\"2025-12-15T10:00:00\","
	                   + "\"departureTime\":\"2025-12-15T07:00:00\","
	                   + "\"availableSeats\":100,"
	                   + "\"price\":{\"oneWay\":100,\"roundTrip\":200}}"))
	        .andExpect(status().isOk());
	}


	@Test
	void testUpdateFlightEndpointNotFound() throws Exception {
		when(flightService.updateFlight(eq("FL999"), any(Flight.class))).thenReturn(ResponseEntity.notFound().build());

		mockMvc.perform(
				put("/flights/FL999").contentType(MediaType.APPLICATION_JSON).content("{\"airlineId\":\"AI001\"}"))
				.andExpect(status().isNotFound());
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
