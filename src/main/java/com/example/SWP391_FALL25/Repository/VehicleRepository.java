package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    Optional<Vehicle> findByVin(String vin);
}
