package com.example.SWP391_FALL25.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ServiceReport")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reportDate;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private ServiceAppointment appointment;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<ServiceReportDetails> details;
}
