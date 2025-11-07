package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ServiceReport,Long> {
    ServiceReport findByAppointment(ServiceAppointment  appointment);
}
