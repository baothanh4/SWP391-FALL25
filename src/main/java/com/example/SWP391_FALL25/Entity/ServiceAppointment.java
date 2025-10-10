package com.example.SWP391_FALL25.Entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;

@Table(name = "ServiceAppointment")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate appointmentDate;

    private String appointmentTime;
    private String technicanAssigned;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    @JsonIgnore
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "service_center_id")
    @JsonIgnore
    private ServiceCenter serviceCenter;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private ServiceReport report;
}
