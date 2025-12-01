package com.flightapp.demo.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Passenger;
import com.flightapp.demo.repository.PassengerRepository;
import com.flightapp.demo.service.implementation.PassengerServiceImplementation;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

	@Mock
	private PassengerRepository passengerRepo;

	@InjectMocks
	private PassengerServiceImplementation passengerService;

	@Test
	void testGetPassengerByIdSuccess() {
		Passenger passenger = new Passenger();
		passenger.setId("123");
		passenger.setName("Hello");
		when(passengerRepo.findById("123")).thenReturn(Optional.of(passenger));
		ResponseEntity<Passenger> response = passengerService.getPassengerById("123");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getId()).isEqualTo("123");
	}

	@Test
	void testGetPassengerByIdNotFound() {
		when(passengerRepo.findById("999")).thenReturn(Optional.empty());
		ResponseEntity<Passenger> response = passengerService.getPassengerById("999");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void testGetPassengerByEmailSuccess() {
		Passenger passenger = new Passenger();
		passenger.setEmail("hello@example.com");
		when(passengerRepo.findByEmail("hello@example.com")).thenReturn(Optional.of(passenger));
		ResponseEntity<Passenger> response = passengerService.getPassengerByEmail("hello@example.com");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getEmail()).isEqualTo("hello@example.com");
	}

	@Test
	void testGetPassengerByEmailNotFound() {
		when(passengerRepo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
		ResponseEntity<Passenger> response = passengerService.getPassengerByEmail("unknown@example.com");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void testSavePassengerSuccess() {
		Passenger passenger = new Passenger();
		passenger.setEmail("new@example.com");
		when(passengerRepo.findByEmail("new@example.com")).thenReturn(Optional.empty());
		ResponseEntity<Void> response = passengerService.savePassenger(passenger);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		verify(passengerRepo).save(passenger);
	}

	@Test
	void testSavePassengerAlreadyExists() {
		Passenger passenger = new Passenger();
		passenger.setEmail("exists@example.com");
		when(passengerRepo.findByEmail("exists@example.com")).thenReturn(Optional.of(passenger));
		ResponseEntity<Void> response = passengerService.savePassenger(passenger);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		verify(passengerRepo, never()).save(passenger);
	}

	@Test
	void testUpdateByIdSuccess() {
		Passenger existing = new Passenger();
		existing.setId("123");
		existing.setName("Old");
		Passenger updated = new Passenger();
		updated.setName("New");
		updated.setAge(25);
		updated.setEmail("new@example.com");
		when(passengerRepo.findById("123")).thenReturn(Optional.of(existing));
		ResponseEntity<Passenger> response = passengerService.updateById("123", updated);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getName()).isEqualTo("New");
		verify(passengerRepo).save(existing);
	}

	@Test
	void testUpdateByIdNotFound() {
		Passenger updated = new Passenger();
		updated.setName("New");
		when(passengerRepo.findById("999")).thenReturn(Optional.empty());
		ResponseEntity<Passenger> response = passengerService.updateById("999", updated);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void testDeleteByIdSuccess() {
		Passenger passenger = new Passenger();
		passenger.setId("123");
		when(passengerRepo.findById("123")).thenReturn(Optional.of(passenger));
		ResponseEntity<Void> response = passengerService.deleteById("123");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(passengerRepo).delete(passenger);
	}

	@Test
	void testDeleteByIdNotFound() {
		when(passengerRepo.findById("999")).thenReturn(Optional.empty());
		ResponseEntity<Void> response = passengerService.deleteById("999");
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		verify(passengerRepo, never()).delete(any());
	}
}
