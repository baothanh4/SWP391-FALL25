package com.example.SWP391_FALL25.DTO.Auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
public class ServiceAppointmentDTO {
    private LocalDate appointmentDate;
    private String appointmentTime;
}
