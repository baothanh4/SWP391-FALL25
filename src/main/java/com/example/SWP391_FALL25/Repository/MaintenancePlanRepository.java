package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaintenancePlanRepository extends JpaRepository<MaintenancePlan,Long> {
    Optional<MaintenancePlan> findByIntervalMonths(Integer intervalMonths);
    Optional<MaintenancePlan> findTopByIntervalKmLessThanEqualOrderByIntervalKmDesc(Integer Od0meter);
    Optional<MaintenancePlan> findTopByIntervalKmGreaterThanOrderByIntervalKmAsc(Integer currentKm);
}
