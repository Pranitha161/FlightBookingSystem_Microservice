package com.flightapp.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.demo.entity.Passenger;
import com.flightapp.demo.repository.PassengerRepository;
import com.flightapp.demo.service.PassengerService;

@WebMvcTest(PassengerController.class)
class PassengerControllerTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	private PassengerService passengerService;
	@InjectMocks
	private PassengerController passengerController;
	@Mock
	private PassengerRepository passengerRepo;

	@Test
	void testGetPassengerByIdSuccess() throws Exception {
		Passenger passenger = new Passenger();
		passenger.setId("123");
		passenger.setAge(20);
		passenger.setName("Hello");
		when(passengerService.getPassengerById("123")).thenReturn(ResponseEntity.status(HttpStatus.OK).body(passenger));
		mockMvc.perform(get("/api/passenger/get/{passengerId}", "123")).andExpect(status().isOk());
	}

	@Test
	void testGetPassengerByIdFailure() throws Exception {

		when(passengerService.getPassengerById("123")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		mockMvc.perform(get("/api/passenger/get/{passengerId}", "123")).andExpect(status().isNotFound());
	}

	@Test
	void testGetPassengerByEmailSuccess() throws Exception {
		Passenger passenger = new Passenger();
		passenger.setId("123");
		passenger.setAge(20);
		passenger.setEmail("hello@example.com");
		passenger.setName("Hello");
		when(passengerService.getPassengerByEmail("hello@example.com"))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body(passenger));
		mockMvc.perform(get("/api/passenger/get/email/{email}", "hello@example.com")).andExpect(status().isOk());
	}

	@Test
	void testGetPassengerByEmailFailure() throws Exception {

		when(passengerService.getPassengerByEmail("hello@example.com"))
				.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		mockMvc.perform(get("/api/passenger/get/email/{email}", "hello@example.com")).andExpect(status().isNotFound());
	}

	@Test
	void testAddPassengerSuccess() throws Exception {
		when(passengerService.savePassenger(any(Passenger.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

		mockMvc.perform(post("/api/passenger/add").contentType(MediaType.APPLICATION_JSON).content(
				"{\"id\":\"123\",\"name\":\"Hello\",\"gender\":\"Female\",\"age\":20,\"email\":\"hello@example.com\",\"bookingId\":\"BKG001\"}"))
				.andExpect(status().isCreated());
	}

	@Test
	void testAddPAssengerFailure() throws Exception {

		mockMvc.perform(post("/api/passenger/add").contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"123\",\"name\":\"Hello\",\"age\":20,\"email\":\"hello@example.com\"}"))
				.andExpect(status().isBadRequest());

	}

	@Test
	void testDeletePassengerSuccess() throws Exception {
		Passenger passenger = new Passenger();
		passenger.setId("123");
		passenger.setAge(20);
		passenger.setEmail("hello@example.com");
		passenger.setName("Hello");
		when(passengerService.deleteById("123")).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
		mockMvc.perform(delete("/api/passenger/delete/{passengerId}", "123")).andExpect(status().isNoContent());
	}

	@Test
	void testDeletePassengerFailure() throws Exception {
		when(passengerService.deleteById("123")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		mockMvc.perform(delete("/api/passenger/delete/{passengerId}", "123")).andExpect(status().isNotFound());
	}

	@Test
	void testUpdatePassenger_Success() throws Exception {
		Passenger passenger = new Passenger();
		passenger.setId("123");
		passenger.setAge(22);
		passenger.setEmail("pranitha@example.com");
		passenger.setName("Pranitha");
		when(passengerService.updateById(eq("123"), any(Passenger.class))).thenReturn(ResponseEntity.ok(passenger));
		mockMvc.perform(
				post("/api/passenger/update/{passengerId}", "123").contentType(MediaType.APPLICATION_JSON).content(
						"{\"id\":\"123\",\"name\":\"Pranitha\",\"gender\":\"Female\",\"age\":23,\"email\":\"pranitha@example.com\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Pranitha"))
				.andExpect(jsonPath("$.email").value("pranitha@example.com")).andExpect(jsonPath("$.age").value(22));
	}

}
