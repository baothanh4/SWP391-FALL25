package com.example.SWP391_FALL25.DTO.Auth;

import lombok.Data;


@Data
public class DashboardStatsDTO {
    private long totalCustomers;
    private long totalStaff;
    private long totalTechnicians;

    // Appointment Stats
    private long totalAppointments;
    private long pendingAppointments;
    private long completedAppointments;

    // Revenue Stats
    private double totalRevenue;

    // Parts Stats
    private long totalParts;
    private long lowStockParts;
}
