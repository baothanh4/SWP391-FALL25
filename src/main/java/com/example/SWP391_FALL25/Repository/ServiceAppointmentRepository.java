package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceAppointmentRepository extends JpaRepository<ServiceAppointment,Long> {
    List<ServiceAppointment> findByTechnicanAssigned(String technicanAssigned);
}
