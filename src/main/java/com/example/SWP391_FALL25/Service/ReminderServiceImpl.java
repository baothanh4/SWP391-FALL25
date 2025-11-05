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

        List<Reminder> reminders = reminderRepository.findByVehicle(vehicle);

        // Cập nhật status nếu cần
        for (Reminder r : reminders) {
            updateReminderStatus(r);
        }

        reminderRepository.saveAll(reminders);

        VehicleMaintenanceResponseDTO response = new VehicleMaintenanceResponseDTO();
        response.setId(vehicle.getId());
        response.setVin(vehicle.getVin());
        response.setPurchaseDate(vehicle.getPurchaseDate());

        List<MaintenanceReminderResponseDTO> reminderDTOs = reminders.stream()
                .map(r -> new MaintenanceReminderResponseDTO(
                        r.getMaintenancePlan().getId().intValue(),
                        r.getReminderDate(),
                        r.getStatus().name()))
                .toList();


        response.setMaintenanceReminders(reminderDTOs);
        return response;
    }

    private void updateReminderStatus(Reminder reminder) {
        LocalDate today = LocalDate.now();
        LocalDate scheduled = reminder.getReminderDate();

        if (reminder.getStatus() == ReminderStatus.DONE) return;

        if (today.isAfter(scheduled)) {
            reminder.setStatus(ReminderStatus.MISSED);
        } else {
            reminder.setStatus(ReminderStatus.PENDING);
        }
    }

    @Override
    public void generateInitialReminders(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

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
