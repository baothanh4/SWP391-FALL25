package com.example.SWP391_FALL25.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import java.util.Map;

public interface PaymentService {
    @Transactional
    String handleVnpayReturn(HttpServletRequest request);

    @Transactional
    void confirmPayment(Long paymentId);

    Map<String, Object> createVNPayPayment(Long paymentId, String paymentMethod);
    
    Map<String, Object> getPaymentDetailsByAppointment(Long appointmentId);
    
    void confirmCashPayment(Long paymentId);
}
