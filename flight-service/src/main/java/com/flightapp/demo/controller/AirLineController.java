package com.flightapp.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.demo.entity.Airline;
import com.flightapp.demo.service.AirLineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1.0/flight/airline")
@RequiredArgsConstructor
public class AirLineController {

	private final AirLineService airlineService;

	@GetMapping("/get")
	public Flux<Airline> getAirlines() {
		return airlineService.getAllAirlines();
	}

	@PostMapping("/add")
	public Mono<ResponseEntity<Void>> addAirline(@Valid @RequestBody Airline airline) {
		return airlineService.addAirline(airline);
	}

}
