package com.flightapp.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.flightapp.demo.dto.AirlineDTO;

@FeignClient(name = "AIRLINE-SERVICE")
public interface AirlineClient {
	@GetMapping("/api/v1.0/flight/airline/{id}")
	AirlineDTO getAirlineById(@PathVariable("id") String id);
}
