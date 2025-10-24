package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.Reminder;
import com.example.SWP391_FALL25.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder,Long> {
    Optional<Reminder> findByVehicleAndStatus(Vehicle vehicle,String status);
}
