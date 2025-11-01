package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.Payment;
import com.example.SWP391_FALL25.Enum.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByAppointmentId(Long appointmentId);
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
    List<Payment> findByStatus(PaymentStatus status);

}
