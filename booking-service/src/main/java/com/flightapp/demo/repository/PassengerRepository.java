package com.flightapp.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flightapp.demo.entity.Passenger;

public interface PassengerRepository extends MongoRepository<Passenger, String> {
	Optional<Passenger> findByEmail(String email);
}
