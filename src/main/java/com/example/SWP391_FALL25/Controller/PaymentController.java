package com.example.SWP391_FALL25.Controller;


import com.example.SWP391_FALL25.Service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> handleVnpayReturn(HttpServletRequest request) {
        String redirectUrl = paymentService.handleVnpayReturn(request);
        return ResponseEntity.status(302).header("Location", redirectUrl).build();
    }

    @PostMapping("/confirm/{paymentId}")
    public ResponseEntity<String> confirmPayment(@PathVariable Long paymentId) {
        paymentService.confirmPayment(paymentId);
        return ResponseEntity.ok("Payment confirmed successfully");
    }

    @PostMapping("/{paymentId}")
    public ResponseEntity<Map<String, Object>> createVnPayUrl(
            @PathVariable Long paymentId,
            @RequestParam(required = false) String paymentMethod
    ) {
        Map<String, Object> response = paymentService.createVNPayPayment(paymentId, paymentMethod);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointment/{appointmentId}/details")
    public ResponseEntity<Map<String, Object>> getPaymentDetailsByAppointment(@PathVariable Long appointmentId) {
        Map<String, Object> details = paymentService.getPaymentDetailsByAppointment(appointmentId);
        return ResponseEntity.ok(details);
    }

    @PostMapping("/cash/{paymentId}")
    public ResponseEntity<String> confirmCashPayment(@PathVariable Long paymentId) {
        paymentService.confirmCashPayment(paymentId);
        return ResponseEntity.ok("Cash payment confirmed successfully");
    }
}
