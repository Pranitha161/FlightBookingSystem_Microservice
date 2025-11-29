package com.flightapp.demo.service.implementation;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Airline;
import com.flightapp.demo.repository.AirLineRepository;
import com.flightapp.demo.service.AirLineService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AirLineServiceImplementation implements AirLineService {
	private final AirLineRepository airlineRepo;

	public Flux<Airline> getAllAirlines() {
		return airlineRepo.findAll();
	}
	 public Mono<Airline> addFlightToAirline(String airlineId, String flightId) {
	        return airlineRepo.findById(airlineId)
	            .flatMap(airline -> {
	                if (airline.getFlightIds() == null) {
	                    airline.setFlightIds(new ArrayList<>());
	                }
	                airline.getFlightIds().add(flightId);
	                return airlineRepo.save(airline);
	            });
	    }
	public Mono<Airline> getById(String id) {
		return airlineRepo.findById(id);
	}

}
