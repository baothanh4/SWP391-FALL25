package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;
    private Long customerId;
    private Long vehicleId;
    private Long serviceCenterId;
    private String status;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String technicianAssigned;

}
