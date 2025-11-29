package com.flightapp.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flightapp.demo.entity.Seat;

public interface SeatRepository extends MongoRepository<Seat, String> {
	List<Seat> findByFlightId(String id);
}
