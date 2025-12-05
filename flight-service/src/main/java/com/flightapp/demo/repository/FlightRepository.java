package com.flightapp.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flightapp.demo.entity.Flight;

public interface FlightRepository extends MongoRepository<Flight, String> {
	List<Flight> getFightByFromPlaceAndToPlace(String fromPlace, String toPlace);

	List<Flight> getByAirlineId(String airlineId);
	
	Optional<Flight> findByAirlineIdAndFromPlaceAndToPlaceAndArrivalTimeAndDepartureTime(String arilineId,String fromPlace,String toPlace,LocalDateTime arrivalTime,LocalDateTime departureTime);
}
