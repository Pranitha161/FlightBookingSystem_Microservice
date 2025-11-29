package com.flightapp.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirlineDTO {
    private String id;
    private String name;
    private String logoUrl;
}

