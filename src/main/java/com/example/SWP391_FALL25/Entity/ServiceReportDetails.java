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
    private String actionType;
    private String conditionStatus;
    private Double laborCost;
    private Double partCost;
    private Double totalCost;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private ServiceReport report;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;                   // Nếu có thay thế phụ tùng

    @ManyToOne
    @JoinColumn(name = "maintenance_plan_item_id")
    private MaintenancePlanItem maintenancePlanItem;  // Liên kết đến hạng mục định kỳ
}
