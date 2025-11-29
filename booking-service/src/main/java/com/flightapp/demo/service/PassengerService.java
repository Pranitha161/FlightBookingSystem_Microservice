package com.flightapp.demo.service;

import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Passenger;

import reactor.core.publisher.Mono;

public interface PassengerService {
	Mono<ResponseEntity<Passenger>> getPassengerById(String passengerId);

	Mono<ResponseEntity<Passenger>> getPassengerByEmail(String email);

	Mono<ResponseEntity<Void>> savePassenger(Passenger passenger);

	Mono<ResponseEntity<Passenger>> updateById(String id, Passenger passenger);

	Mono<ResponseEntity<Void>> deleteById(String passengerId);

}
