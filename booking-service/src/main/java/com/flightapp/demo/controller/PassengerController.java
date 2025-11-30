package com.flightapp.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.demo.entity.Passenger;
import com.flightapp.demo.service.PassengerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/passenger")
public class PassengerController {
	private final PassengerService passengerService;

	@GetMapping("/get/{passengerId}")
	public ResponseEntity<Passenger> getPassengers(@PathVariable String passengerId) {
		return passengerService.getPassengerById(passengerId);
	}

	@GetMapping("/get/email/{email}")
	public ResponseEntity<Passenger> getPassenger(@PathVariable String email) {
		return passengerService.getPassengerByEmail(email);
	}

	@PostMapping("/add")
	public ResponseEntity<Void> addPassenger(@RequestBody @Valid Passenger p) {
		return passengerService.savePassenger(p);
	}

	@PostMapping("/update/{passengerId}")
	public ResponseEntity<Passenger> updatePassenger(@PathVariable String passengerId,
			@RequestBody @Valid Passenger p) {
		return passengerService.updateById(passengerId, p);
	}

	@DeleteMapping("/delete/{passengerId}")
	public ResponseEntity<Void> deletePassenger(@PathVariable String passengerId) {
		return passengerService.deleteById(passengerId);
	}

}
