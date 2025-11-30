package com.flightapp.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flightapp.demo.entity.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {
	Optional<Booking> findByPnr(String pnr);

	Optional<Booking> findByEmail(String email);
}


