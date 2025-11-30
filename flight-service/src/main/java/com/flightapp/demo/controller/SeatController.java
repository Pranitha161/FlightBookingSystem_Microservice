package com.flightapp.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.demo.entity.Seat;
import com.flightapp.demo.service.SeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

	private final SeatService seatService;

	@GetMapping("/flight/{flightId}")
	public ResponseEntity<List<Seat>> getSeatsByFlightId(@PathVariable String flightId) {
		List<Seat> seats = seatService.getSeatsByFlightId(flightId);
		if (seats.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(seats);
	}

	@PutMapping("/flights/{id}/seats")
	public ResponseEntity<Void> updateSeats(@PathVariable String id, @RequestBody List<Seat> seats) {
		boolean updated = seatService.updateSeats(id, seats);
		return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}
}
