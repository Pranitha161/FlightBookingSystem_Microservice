package com.flightapp.demo.service.implementation;

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

	public Mono<Airline> getById(String id) {
		return airlineRepo.findById(id);
	}

}
