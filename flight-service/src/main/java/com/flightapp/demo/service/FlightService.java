package com.flightapp.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.SearchRequest;

public interface FlightService {

	ResponseEntity<List<Flight>> search(SearchRequest searchRequest);

	ResponseEntity<Void> addFlight(Flight flightTest);

	List<Flight> getFlights();

	ResponseEntity<Flight> getFlightById(String flightId);

	ResponseEntity<Void> updateFlight(String id, Flight flightTest);

}
