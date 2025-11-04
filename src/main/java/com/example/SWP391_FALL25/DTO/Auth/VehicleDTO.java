package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private Long id;
    private String vin;
    private String licensePlate;
    private String brand;
    private String model;
    private LocalDate purchaseDate;
    private Integer odometer;

}
