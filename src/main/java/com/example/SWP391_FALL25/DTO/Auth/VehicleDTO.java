package com.example.SWP391_FALL25.DTO.Auth;

import lombok.Data;

@Data
public class VehicleDTO {


    private String vin;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
    private String odometer;

}
