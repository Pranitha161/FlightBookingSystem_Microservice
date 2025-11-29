package com.flightapp.demo.service;

import java.util.List;

import com.flightapp.demo.entity.Seat;

public interface SeatService {

	List<Seat> initialiszeSeats(String flightId, int rows, int cols);
}
