package com.flightapp.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flightapp.demo.entity.Seat;
import com.flightapp.demo.repository.SeatRepository;
import com.flightapp.demo.service.implementation.SeatServiceImplementation;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

	@Mock
	private SeatRepository seatRepo;
	@InjectMocks
	private SeatServiceImplementation seatService;
	@Test
	void testGetSeatsByFlightId() {
		Seat seat = new Seat();
		seat.setSeatNumber("1A");
		seat.setFlightId("F1");
		seat.setAvailable(true);
		when(seatRepo.findByFlightId("F1")).thenReturn(Arrays.asList(seat));
		List<Seat> result = seatService.getSeatsByFlightId("F1");
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getSeatNumber()).isEqualTo("1A");
		verify(seatRepo).findByFlightId("F1");
	}

	@Test
	void testInitialiszeSeats() {
		when(seatRepo.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
		List<Seat> result = seatService.initialiszeSeats("F1", 2, 6);
		assertThat(result).hasSize(12); // 2 rows × 6 cols
		assertThat(result.get(0).getSeatNumber()).isEqualTo("1A");
		assertThat(result.get(5).getSeatNumber()).isEqualTo("1F");
		assertThat(result.get(6).getSeatNumber()).isEqualTo("2A");
		verify(seatRepo).saveAll(anyList());
	}

	@Test
	void testUpdateSeatsSuccess() {
		Seat seat = new Seat();
		seat.setSeatNumber("1A");
		seat.setAvailable(false);
		when(seatRepo.findByFlightId("F1")).thenReturn(Arrays.asList(seat));
		when(seatRepo.saveAll(anyList())).thenReturn(Arrays.asList(seat));
		boolean result = seatService.updateSeats("F1", Arrays.asList(seat));
		assertThat(result).isTrue();
		verify(seatRepo).saveAll(Arrays.asList(seat));
	}

	@Test
	void testUpdateSeatsNotFound() {
		when(seatRepo.findByFlightId("F999")).thenReturn(Collections.emptyList());
		boolean result = seatService.updateSeats("F999", Arrays.asList(new Seat()));
		assertThat(result).isFalse();
		verify(seatRepo, never()).saveAll(anyList());
	}
}
