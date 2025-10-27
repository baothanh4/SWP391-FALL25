package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByAppointmentId(Long appointmentId);

}
