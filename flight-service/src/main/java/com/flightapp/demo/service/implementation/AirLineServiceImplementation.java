package com.flightapp.demo.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Airline;
import com.flightapp.demo.repository.AirLineRepository;
import com.flightapp.demo.service.AirLineService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirLineServiceImplementation implements AirLineService {
	private final AirLineRepository airlineRepo;

	public List<Airline> getAllAirlines() {
		return airlineRepo.findAll();
	}
	public Optional<Airline> addFlightToAirline(String airlineId, String flightId) {
	    return airlineRepo.findById(airlineId)
	        .map(airline -> {
	            if (airline.getFlightIds() == null) {
	                airline.setFlightIds(new ArrayList<>());
	            }
	            airline.getFlightIds().add(flightId);
	            return airlineRepo.save(airline); 
	        });
	}
	public Optional<Airline> getById(String id) {
		return airlineRepo.findById(id);
	}

}
