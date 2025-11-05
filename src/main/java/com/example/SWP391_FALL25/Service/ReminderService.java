package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.VehicleMaintenanceResponseDTO;

public interface ReminderService {
    VehicleMaintenanceResponseDTO getVehicleMaintenance(Long vehicleId);

    void generateInitialReminders(Long vehicle);
}
