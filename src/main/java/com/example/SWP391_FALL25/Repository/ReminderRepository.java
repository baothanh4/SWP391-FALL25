package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.MaintenancePlan;
import com.example.SWP391_FALL25.Entity.Reminder;
import com.example.SWP391_FALL25.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder,Long> {
    Optional<Reminder> findByVehicleAndStatus(Vehicle vehicle,String status);
    List<Reminder> findByVehicle(Vehicle vehicle);
    boolean existsByVehicleAndMaintenancePlan(Vehicle vehicle, MaintenancePlan maintenancePlan);
}
