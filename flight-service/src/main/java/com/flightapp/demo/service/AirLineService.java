package com.flightapp.demo.service;

import com.flightapp.demo.entity.Airline;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AirLineService {
	Flux<Airline> getAllAirlines();

	Mono<Airline> getById(String name);

}

