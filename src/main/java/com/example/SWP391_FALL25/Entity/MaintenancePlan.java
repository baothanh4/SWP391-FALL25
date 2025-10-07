package com.example.SWP391_FALL25.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "MaintenancePlan")
@Data
public class MaintenancePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;
    private Integer intervalKm;
    private Integer intervalMonths;

    @OneToMany(mappedBy = "maintenancePlan", cascade = CascadeType.ALL)
    private List<MaintenancePlanItem> items;

    @OneToMany(mappedBy = "maintenancePlan", cascade = CascadeType.ALL)
    private List<Reminder> reminders;
}
