package com.flightapp.demo.service.implementation;


import java.util.List;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FlightServiceImplementation implements FlightService {

	private final FlightRepository flightRepo;
	private final AirLineRepository airlineRepo;
	private final SeatService seatService;
	private final AirLineService airlineService;

	@Override // Mono because we want to send only one https response for entire request
	public Mono<ResponseEntity<List<Flight>>> search(SearchRequest searchRequest) {
		return flightRepo.getFightByFromPlaceAndToPlace(searchRequest.getFromPlace(), searchRequest.getToPlace())
				.filter(flight -> flight.getArrivalTime().toLocalDate().equals(searchRequest.getDate())).collectList()
				.map(list -> list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list));
	}

	@Override
	public Mono<ResponseEntity<Void>> addFlight(Flight flight) {
	    final int cols = 6;
	    if (flight.getAvailableSeats() <= 0 || flight.getAvailableSeats() % cols != 0) {
	        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).<Void>build());
	    }
	    final int rows = flight.getAvailableSeats() / cols;
	    return airlineRepo.findById(flight.getAirlineId()) 
	            .flatMap(existingAirline -> flightRepo.save(flight)
	                .flatMap(savedFlight -> seatService.initialiszeSeats(savedFlight.getId(), rows, cols)
	                		.then(airlineService.addFlightToAirline(savedFlight.getAirlineId(), savedFlight.getId()))
	                    .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).<Void>build())))
	            )
	            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).<Void>build()));
		
	}


	@Override
	public Flux<Flight> getFlights() {
		return flightRepo.findAll();
	}

	@Override
	public Mono<ResponseEntity<Flight>> getFlightById(String flightId) {
		return flightRepo.findById(flightId).map(ResponseEntity::ok)
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
}
