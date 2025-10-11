package com.example.SWP391_FALL25.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "MaintenancePlanItem")
@AllArgsConstructor
@NoArgsConstructor
public class MaintenancePlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;

    private String partType;

    @ManyToOne
    @JoinColumn(name = "maintenance_plan_id")
    @JsonIgnore
    private MaintenancePlan maintenancePlan;
}
