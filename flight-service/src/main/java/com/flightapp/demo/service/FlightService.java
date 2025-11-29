package com.flightapp.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.SearchRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlightService {
	Mono<ResponseEntity<List<Flight>>> search(SearchRequest searchRequest);

	Mono<ResponseEntity<Void>> addFlight(Flight flight);

	Flux<Flight> getFlights();

	Mono<ResponseEntity<Flight>> getFlightById(String flightId);

}
