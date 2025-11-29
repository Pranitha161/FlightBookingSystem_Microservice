package com.flightapp.demo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.flightapp.demo.entity.Passenger;

import reactor.core.publisher.Mono;

public interface PassengerRepository extends ReactiveMongoRepository<Passenger, String> {
	Mono<com.flightapp.demo.entity.Passenger> findByEmail(String email);
}
