package com.flightapp.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Airline;
import com.flightapp.demo.repository.AirlineRepository;
import com.flightapp.demo.service.implementation.AirLineServiceImplementation;

@ExtendWith(MockitoExtension.class)
class AirLineServiceTest {

	@Mock
	private AirlineRepository airlineRepo;

	@InjectMocks
	private AirLineServiceImplementation airLineService;

	@Test
	void testGetAllAirlines() {
		Airline airline1 = new Airline();
		airline1.setId("A1");
		Airline airline2 = new Airline();
		airline2.setId("A2");

		when(airlineRepo.findAll()).thenReturn(Arrays.asList(airline1, airline2));

		var result = airLineService.getAllAirlines();

		assertThat(result).hasSize(2);
		assertThat(result).extracting(Airline::getId).contains("A1", "A2");
		verify(airlineRepo).findAll();
	}

	@Test
	void testAddFlightToAirlineSuccess() {
		Airline airline = new Airline();
		airline.setId("A1");

		when(airlineRepo.findById("A1")).thenReturn(Optional.of(airline));
		when(airlineRepo.save(any(Airline.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Optional<Airline> result = airLineService.addFlightToAirline("A1", "F123");

		assertThat(result).isPresent();
		assertThat(result.get().getFlightIds()).contains("F123");
		verify(airlineRepo).save(airline);
	}

	@Test
	void testAddFlightToAirlineNotFound() {
		when(airlineRepo.findById("A999")).thenReturn(Optional.empty());

		Optional<Airline> result = airLineService.addFlightToAirline("A999", "F123");

		assertThat(result).isEmpty();
		verify(airlineRepo, never()).save(any(Airline.class));
	}

	@Test
	void testGetByIdSuccess() {
		Airline airline = new Airline();
		airline.setId("A1");

		when(airlineRepo.findById("A1")).thenReturn(Optional.of(airline));

		ResponseEntity<Airline> response = airLineService.getById("A1");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo("A1");
	}

	@Test
	void testGetByIdNotFound() {
		when(airlineRepo.findById("A999")).thenReturn(Optional.empty());

		ResponseEntity<Airline> response = airLineService.getById("A999");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isNull();
	}
}
