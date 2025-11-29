package com.flightapp.demo.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Passenger;
import com.flightapp.demo.repository.PassengerRepository;
import com.flightapp.demo.service.PassengerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PassengerServiceImplementation implements PassengerService {
	private final PassengerRepository passengerRepo;

	public ResponseEntity<Passenger> getPassengerById(String passengerId) {
		return passengerRepo.findById(passengerId).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	public ResponseEntity<Passenger> getPassengerByEmail(String email) {
		return passengerRepo.findByEmail(email).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	public ResponseEntity<Void> savePassenger(Passenger passenger) {
		return passengerRepo.findByEmail(passenger.getEmail())
				.map(exists -> ResponseEntity.badRequest().<Void>build())
				.orElseGet(()->{passengerRepo
						.save(passenger);
				return ResponseEntity.status(HttpStatus.CREATED).<Void>build();});
	}

	public ResponseEntity<Passenger> updateById(String id, Passenger passenger) {
		return passengerRepo.findById(id).map(existing -> {
			existing.setAge(passenger.getAge());
			existing.setEmail(passenger.getEmail());
			existing.setName(passenger.getName());
			 passengerRepo.save(existing);
			 return ResponseEntity.ok(existing);
		}).orElseGet(()->ResponseEntity.badRequest().<Passenger>build());
	}

	public ResponseEntity<Void> deleteById(String passengerId) {
		return passengerRepo.findById(passengerId).map(existing -> {
			   passengerRepo.delete(existing);
			   return ResponseEntity.noContent().<Void>build();
			   }).orElseGet(()->ResponseEntity.notFound().<Void>build());
	}

}
