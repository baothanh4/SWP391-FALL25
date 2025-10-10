package com.example.SWP391_FALL25.DTO.Auth;


import lombok.Data;

@Data
public class ServiceReportDetailDTO {
    private Long partId;
    private Long maintenanceItemId;
    private String service;
    private String actionType;
    private String conditionStatus;
    private double laborCost;
    private double partCost;
}
