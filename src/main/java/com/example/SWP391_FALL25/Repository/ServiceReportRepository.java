package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.ServiceReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceReportRepository extends JpaRepository<ServiceReport, Long> {
    Optional<ServiceReport> findByAppointmentId(Long appointmentId);
}
