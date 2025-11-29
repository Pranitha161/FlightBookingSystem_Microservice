package com.flightapp.demo.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Passenger;
import com.flightapp.demo.repository.PassengerRepository;
import com.flightapp.demo.service.PassengerService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PassengerServiceImplementation implements PassengerService {
	private final PassengerRepository passengerRepo;

	public Mono<ResponseEntity<Passenger>> getPassengerById(String passengerId) {
		return passengerRepo.findById(passengerId).map(ResponseEntity::ok)
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}

	public Mono<ResponseEntity<Passenger>> getPassengerByEmail(String email) {
		return passengerRepo.findByEmail(email).map(ResponseEntity::ok)
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}

	public Mono<ResponseEntity<Void>> savePassenger(Passenger passenger) {
		return passengerRepo.findByEmail(passenger.getEmail())
				.flatMap(exists -> Mono.just(ResponseEntity.badRequest().<Void>build())).switchIfEmpty(passengerRepo
						.save(passenger).then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).<Void>build())));
	}

	public Mono<ResponseEntity<Passenger>> updateById(String id, Passenger passenger) {
		return passengerRepo.findById(id).flatMap(existing -> {
			existing.setAge(passenger.getAge());
			existing.setEmail(passenger.getEmail());
			existing.setName(passenger.getName());
			return passengerRepo.save(existing).map(ResponseEntity::ok);
		}).switchIfEmpty(Mono.just(ResponseEntity.badRequest().<Passenger>build()));
	}

	public Mono<ResponseEntity<Void>> deleteById(String passengerId) {
		return passengerRepo.findById(passengerId).flatMap(
				existing -> passengerRepo.delete(existing).then(Mono.just(ResponseEntity.noContent().<Void>build())))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().<Void>build()));
	}

}
