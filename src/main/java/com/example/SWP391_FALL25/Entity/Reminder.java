package com.example.SWP391_FALL25.Entity;


import com.example.SWP391_FALL25.Enum.ReminderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "Reminder")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reminderDate;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "maintenance_plan_id")
    @JsonIgnore
    private MaintenancePlan maintenancePlan;
}
