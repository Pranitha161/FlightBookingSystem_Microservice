package com.flightapp.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.Price;
import com.flightapp.demo.entity.SearchRequest;
import com.flightapp.demo.repository.AirlineRepository;
import com.flightapp.demo.repository.FlightRepository;
import com.flightapp.demo.service.implementation.FlightServiceImplementation;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

	@Mock
	private FlightRepository flightRepo;
	@Mock
	private AirlineRepository airlineRepo;
	@Mock
	private SeatService seatService;
	@Mock
	private AirLineService airlineService;

	@InjectMocks
	private FlightServiceImplementation flightService;

	@Test
	void testSearchFlightsFound() {
		Flight flight = new Flight();
		flight.setId("F1");
		flight.setArrivalTime(LocalDateTime.of(2025, 12, 2, 10, 0));
		flight.setFromPlace("HYD");
		flight.setToPlace("DEL");

		SearchRequest request = new SearchRequest();
		request.setFromPlace("HYD");
		request.setToPlace("DEL");
		request.setDate(LocalDate.of(2025, 12, 2));
		when(flightRepo.getFightByFromPlaceAndToPlace("HYD", "DEL")).thenReturn(Arrays.asList(flight));
		ResponseEntity<List<Flight>> response = flightService.search(request);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).hasSize(1);
	}

	@Test
	void testSearchFlightsNoContent() {
		SearchRequest request = new SearchRequest();
		request.setFromPlace("HYD");
		request.setToPlace("DEL");
		request.setDate(LocalDate.of(2025, 12, 2));
		when(flightRepo.getFightByFromPlaceAndToPlace("HYD", "DEL")).thenReturn(Collections.emptyList());
		ResponseEntity<List<Flight>> response = flightService.search(request);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	void testAddFlightSuccess() {
		Flight flight = new Flight();
		flight.setId("F1");
		flight.setAirlineId("A1");
		flight.setAvailableSeats(12);
		when(airlineRepo.findById("A1")).thenReturn(Optional.of(new com.flightapp.demo.entity.Airline()));
		when(flightRepo.save(flight)).thenReturn(flight);
		ResponseEntity<String> response = flightService.addFlight(flight);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		verify(seatService).initialiszeSeats("F1", 2, 6);
		verify(airlineService).addFlightToAirline("A1", "F1");
	}

	@Test
	void testAddFlightInvalidSeats() {
		Flight flight = new Flight();
		flight.setAirlineId("A1");
		flight.setAvailableSeats(5); 
		ResponseEntity<String> response = flightService.addFlight(flight);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		verifyNoInteractions(seatService, airlineService);
	}

	@Test
	void testGetFlights() {
		Flight flight = new Flight();
		flight.setId("F1");
		when(flightRepo.findAll()).thenReturn(Arrays.asList(flight));
		var result = flightService.getFlights();
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getId()).isEqualTo("F1");
	}

	@Test
	void testGetFlightByIdSuccess() {
		Flight flight = new Flight();
		flight.setId("F1");
		when(flightRepo.findById("F1")).thenReturn(Optional.of(flight));
		ResponseEntity<Flight> response = flightService.getFlightById("F1");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo("F1");
	}

	@Test
	void testGetFlightByIdNotFound() {
		when(flightRepo.findById("F999")).thenReturn(Optional.empty());
		ResponseEntity<Flight> response = flightService.getFlightById("F999");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void testUpdateFlightSuccess() {
		Flight existing = new Flight();
		existing.setId("F1");
		Flight update = new Flight();
		update.setAirlineId("A1");
		update.setArrivalTime(LocalDateTime.now());
		update.setDepartureTime(LocalDateTime.now().plusHours(2));
		update.setPrice(new Price());
		when(flightRepo.findById("F1")).thenReturn(Optional.of(existing));
		ResponseEntity<Void> response = flightService.updateFlight("F1", update);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(flightRepo).save(existing);
	}

	@Test
	void testUpdateFlightNotFound() {
		Flight update = new Flight();
		update.setAirlineId("A1");
		when(flightRepo.findById("F999")).thenReturn(Optional.empty());
		ResponseEntity<Void> response = flightService.updateFlight("F999", update);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
