package com.flightapp.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flightapp.demo.entity.Airline;

public interface AirLineRepository extends MongoRepository<Airline, String> {
	Optional<Airline> findById(String name);
}
