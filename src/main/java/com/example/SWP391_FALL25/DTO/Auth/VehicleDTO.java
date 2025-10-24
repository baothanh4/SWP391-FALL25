package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private Long id;
    private String vin;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
    private Integer odometer;

}
