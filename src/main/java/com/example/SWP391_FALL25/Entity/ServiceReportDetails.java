package com.example.SWP391_FALL25.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "ServiceReportDetails")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReportDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String service;
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private ServiceReport report;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;
}
