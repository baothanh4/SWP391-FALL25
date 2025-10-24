package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.MaintenancePlan;
import com.example.SWP391_FALL25.Entity.MaintenancePlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenancePlanItemRepository extends JpaRepository<MaintenancePlanItem,Long> {
    List<MaintenancePlanItem> findByMaintenancePlan(MaintenancePlan plan);
}
