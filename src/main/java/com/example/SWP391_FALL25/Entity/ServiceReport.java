package com.example.SWP391_FALL25.Entity;


import com.example.SWP391_FALL25.Enum.QuotationStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(columnDefinition = "TEXT")
    private String customerFeedback;

    private Boolean customerApproved;

    private LocalDateTime customerFeedbackDate;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    @JsonIgnore
    private ServiceAppointment appointment;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ServiceReportDetails> details;
}
