package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceAppointmentRepository extends JpaRepository<ServiceAppointment,Long>, JpaSpecificationExecutor<ServiceAppointment> {
    List<ServiceAppointment> findByTechnicianAssigned(String fullName);
    @Query("SELECT a FROM ServiceAppointment a JOIN a.vehicle v WHERE v.customer.id = :userId")
    List<ServiceAppointment> findByUserId(@Param("userId") Long userId);
}
