package com.flightapp.demo.service.implementation;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Flight;
import com.flightapp.demo.entity.SearchRequest;
import com.flightapp.demo.repository.AirLineRepository;
import com.flightapp.demo.repository.FlightRepository;
import com.flightapp.demo.service.AirLineService;
import com.flightapp.demo.service.FlightService;
import com.flightapp.demo.service.SeatService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightServiceImplementation implements FlightService {

	private final FlightRepository flightRepo;
	private final AirLineRepository airlineRepo;
	private final SeatService seatService;
	private final AirLineService airlineService;

	@Override // Mono because we want to send only one https response for entire request
	public ResponseEntity<List<Flight>> search(SearchRequest searchRequest) {
	    List<Flight> flights = flightRepo.getFightByFromPlaceAndToPlace(
	            searchRequest.getFromPlace(),
	            searchRequest.getToPlace()
	    );

	    List<Flight> filtered = flights.stream()
	        .filter(flight -> flight.getArrivalTime().toLocalDate().equals(searchRequest.getDate()))
	        .collect(Collectors.toList());

	    if (filtered.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    } else {
	        return ResponseEntity.ok(filtered);
	    }
	}

	@Override
	public ResponseEntity<Void> addFlight(Flight flight) {
	    final int cols = 6;
	    if (flight.getAvailableSeats() <= 0 || flight.getAvailableSeats() % cols != 0) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	    }
	    final int rows = flight.getAvailableSeats() / cols;
	    return airlineRepo.findById(flight.getAirlineId())
	        .map(existingAirline -> {
	            Flight savedFlight = flightRepo.save(flight);
	            seatService.initialiszeSeats(savedFlight.getId(), rows, cols);
	            airlineService.addFlightToAirline(savedFlight.getAirlineId(), savedFlight.getId());
	            return ResponseEntity.status(HttpStatus.CREATED).<Void>build();
	        })
	        .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}


	@Override
	public List<Flight> getFlights() {
		return flightRepo.findAll();
	}

	@Override
	public ResponseEntity<Flight> getFlightById(String flightId) {
		return flightRepo.findById(flightId).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().<Flight>build());
	}
}
