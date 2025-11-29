package com.flightapp.demo.service;

import com.flightapp.demo.entity.Seat;

import reactor.core.publisher.Flux;

public interface SeatService {

	Flux<Seat> initialiszeSeats(String flightId, int rows, int cols);
}
