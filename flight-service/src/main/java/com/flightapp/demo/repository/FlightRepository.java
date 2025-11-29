package com.flightapp.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flightapp.demo.entity.Flight;

public interface FlightRepository extends MongoRepository<Flight, String> {
	List<Flight> getFightByFromPlaceAndToPlace(String fromPlace, String toPlace);

	List<Flight> getByAirlineId(String airlineId);
}
