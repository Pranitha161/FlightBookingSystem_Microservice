package com.flightapp.demo.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	public Mono<ResponseEntity<Void>> addAirline(Airline airline) {
		return airlineRepo.findByName(airline.getName())
				.flatMap(existing -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).<Void>build()))
				.switchIfEmpty(airlineRepo.save(airline)
						.map(saved -> ResponseEntity.status(HttpStatus.CREATED).<Void>build()));
	}
}
