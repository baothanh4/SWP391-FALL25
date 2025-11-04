package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.MaintenanceReminderResponseDTO;
import com.example.SWP391_FALL25.DTO.Auth.VehicleMaintenanceResponseDTO;
import com.example.SWP391_FALL25.Entity.MaintenancePlan;
import com.example.SWP391_FALL25.Entity.Reminder;
import com.example.SWP391_FALL25.Entity.Vehicle;
import com.example.SWP391_FALL25.Enum.ReminderStatus;
import com.example.SWP391_FALL25.Repository.MaintenancePlanRepository;
import com.example.SWP391_FALL25.Repository.ReminderRepository;
import com.example.SWP391_FALL25.Repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    @Autowired
    private final VehicleRepository vehicleRepository;

    @Autowired
    private final ReminderRepository reminderRepository;

    @Autowired
    private MaintenancePlanRepository maintenancePlanRepository;

    public VehicleMaintenanceResponseDTO getVehicleMaintenance(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        generateRemindersIfNeeded(vehicle);

        List<Reminder> reminders = reminderRepository.findByVehicle(vehicle);

        VehicleMaintenanceResponseDTO response = new VehicleMaintenanceResponseDTO();
        response.setId(vehicle.getId());
        response.setVin(vehicle.getVin());
        response.setPurchaseDate(vehicle.getPurchaseDate());

        List<MaintenanceReminderResponseDTO> reminderDTOs = new ArrayList<>();

        for (Reminder r : reminders) {
            MaintenanceReminderResponseDTO dto = new MaintenanceReminderResponseDTO();
            dto.setMaintenanceNumber(r.getMaintenancePlan().getId().intValue());
            dto.setScheduledDate(r.getReminderDate());
            dto.setStatus(r.getStatus().name());
            reminderDTOs.add(dto);
        }

        response.setMaintenanceReminders(reminderDTOs);
        return response;
    }

    private void generateRemindersIfNeeded(Vehicle vehicle) {
        List<MaintenancePlan> plans = maintenancePlanRepository.findAll();
        LocalDate purchaseDate = vehicle.getPurchaseDate();

        for (MaintenancePlan plan : plans) {
            if (!reminderRepository.existsByVehicleAndMaintenancePlan(vehicle, plan)) {

                LocalDate date = purchaseDate.plusMonths(plan.getIntervalMonths());

                Reminder reminder = new Reminder();
                reminder.setVehicle(vehicle);
                reminder.setMaintenancePlan(plan);
                reminder.setReminderDate(date);
                reminder.setStatus(ReminderStatus.PENDING);

                reminderRepository.save(reminder);
            }
        }
    }
}
