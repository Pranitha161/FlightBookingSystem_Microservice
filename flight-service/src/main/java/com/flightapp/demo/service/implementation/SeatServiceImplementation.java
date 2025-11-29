package com.flightapp.demo.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.flightapp.demo.entity.Seat;
import com.flightapp.demo.repository.SeatRepository;
import com.flightapp.demo.service.SeatService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatServiceImplementation implements SeatService {
	private final SeatRepository seatRepo;

	public List<Seat> initialiszeSeats(String flightId, int rows, int cols) {
		char[] seatLetters = { 'A', 'B', 'C', 'D', 'E', 'F' };
		List<Seat> seats = new ArrayList<>();
		for (int row = 1; row <= rows; row++) {
			for (int col = 0; col < cols; col++) {
				Seat seat = new Seat();
				seat.setSeatNumber(row + "" + seatLetters[col]);
				seat.setAvailable(true);
				seat.setFlightId(flightId);
				seats.add(seat);
			}
		}
		return seatRepo.saveAll(seats);
	}
}
