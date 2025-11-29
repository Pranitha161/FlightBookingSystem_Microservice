package com.flightapp.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.SearchRequest;
import com.flightapp.demo.service.FlightService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/flight/")
public class FlightController {
	private final FlightService flightService;

	@PostMapping("/search")
	public Mono<ResponseEntity<List<Flight>>> searchFlight(@RequestBody SearchRequest searchRequest) {
		return flightService.search(searchRequest);
	}

	@GetMapping("/get/{flightId}")
	public Mono<ResponseEntity<Flight>> getFlight(@PathVariable String flightId) {
		return flightService.getFlightById(flightId);
	}

	@GetMapping("/get/flights")
	public Flux<Flight> getAllFlights() {
		return flightService.getFlights();
	}
}
