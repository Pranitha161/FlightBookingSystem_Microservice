package com.flightapp.demo.service;

import java.util.List;
import java.util.Optional;

import com.flightapp.demo.entity.Airline;

public interface AirLineService {
	List<Airline> getAllAirlines();

	Optional<Airline> getById(String name);

	Optional<Airline> addFlightToAirline(String airlineId, String flightId);

}
