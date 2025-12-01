package com.flightapp.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.flightapp.demo.entity.Airline;
import com.flightapp.demo.service.AirLineService;

@WebMvcTest(AirLineController.class)
class AirLineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirLineService airlineService;

    @Test
    void testGetAllAirlines_Positive() throws Exception {
        Airline airline = new Airline();
        airline.setId("1");
        airline.setName("Indigo");
        when(airlineService.getAllAirlines()).thenReturn(List.of(airline));

        mockMvc.perform(get("/api/flight/airline/get"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value("1"))
               .andExpect(jsonPath("$[0].name").value("Indigo"));
    }


    @Test
    void testGetAirlineById_NotFound() throws Exception {
        when(airlineService.getById("99")).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/flight/airline/get/99"))
               .andExpect(status().isNotFound());
    }
}
