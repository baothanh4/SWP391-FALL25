package com.example.SWP391_FALL25.DTO.Auth;


import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class VehicleMaintenanceResponseDTO {
    private Long id;
    private String vin;
    private LocalDate purchaseDate;
    private List<MaintenanceReminderResponseDTO> maintenanceReminders;
}
