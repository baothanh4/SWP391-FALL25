package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private Long vehicleId;
    private String vehicleModel;
    private String licensePlate;
    private String vin;
    private Long serviceCenterId;
    private String serviceCenterName;
    private String serviceType;
    private String status;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String technicianAssigned;
    private Long technicianId;
    private String technicianName;
    private String technicianPhone;
    private String priority;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean canCancel; // Indicates if customer can cancel this appointment
    
    // Payment information
    private Double paymentAmount;
    private String paymentStatus;
    private String paymentMethod;
    
    // Nested objects for maintenance report screen
    private VehicleDTO vehicle;
    private ReportDTO report;
    
    // Nested DTO classes
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VehicleDTO {
        private Long id;
        private String vin;
        private String licensePlate;
        private String brand;
        private String model;
        private Integer year;
        private Integer odometer;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReportDTO {
        private Long id;
        private Integer currentKm;
        private LocalDate reportDate;
    }

}
