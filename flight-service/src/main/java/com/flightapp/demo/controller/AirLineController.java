package com.flightapp.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.demo.entity.Airline;
import com.flightapp.demo.service.AirLineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flight/airline")
@RequiredArgsConstructor
public class AirLineController {

	private final AirLineService airlineService;

	@GetMapping("/get")
	public List<Airline> getAirlines() {
		return airlineService.getAllAirlines();
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Airline> getAirlineById(@Valid @PathVariable String id) {
		return airlineService.getById(id);
	}

}
