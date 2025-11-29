package com.flightapp.demo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.flightapp.demo.entity.Airline;

import reactor.core.publisher.Mono;

public interface AirLineRepository extends ReactiveMongoRepository<Airline, String> {
	Mono<Airline> findById(String name);
}
