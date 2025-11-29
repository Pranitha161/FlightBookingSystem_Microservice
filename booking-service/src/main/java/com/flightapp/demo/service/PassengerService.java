package com.flightapp.demo.service;

import org.springframework.http.ResponseEntity;

import com.flightapp.demo.entity.Passenger;

public interface PassengerService {
	
	ResponseEntity<Passenger> getPassengerById(String passengerId);

	ResponseEntity<Passenger> getPassengerByEmail(String email);

	ResponseEntity<Void> savePassenger(Passenger passenger);

	ResponseEntity<Passenger> updateById(String id, Passenger passenger);

	ResponseEntity<Void> deleteById(String passengerId);

}
