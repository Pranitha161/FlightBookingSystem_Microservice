package com.flightapp.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flightapp.demo.entity.Airline;

public interface AirlineRepository extends MongoRepository<Airline, String> {
	Optional<Airline> findByName(String name);
}
