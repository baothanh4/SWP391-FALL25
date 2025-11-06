package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Entity.Payment;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReport;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Enum.PaymentStatus;
import com.example.SWP391_FALL25.Repository.PaymentRepository;
import com.example.SWP391_FALL25.Repository.ServiceAppointmentRepository;
import com.example.SWP391_FALL25.Repository.ServiceReportDetailsRepository;
import com.example.SWP391_FALL25.Utility.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @Autowired
    private VNPayUtils vnPayUtils;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @Autowired
    private ServiceReportDetailsRepository serviceReportDetailsRepository;

    private static final String VNP_HASH_SECRET="GNPMXK160WDIPNTPV5D5AZ29BLXTHDP7";

    @Transactional
    @Override
    public String handleVnpayReturn(HttpServletRequest request) {
        Map<String, String> vnpParams = VNPayUtils.getVNPayResponseParams(request);
        String vnpSecureHash = vnpParams.get("vnp_SecureHash");


        boolean isValid = VNPayUtils.verifySignature(vnpParams, vnpSecureHash, VNP_HASH_SECRET);
        if (!isValid) {
            throw new RuntimeException("Invalid signature");
        }


        String responseCode = vnpParams.get("vnp_ResponseCode");
        if (!"00".equals(responseCode)) {
            throw new RuntimeException("Payment failed with code: " + responseCode);
        }


        String orderInfo = vnpParams.get("vnp_OrderInfo").replace("Thanh toan cho ma GD: ", "").trim();
        Long paymentId = Long.parseLong(orderInfo);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);


        ServiceAppointment appointment = payment.getAppointment();
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            serviceAppointmentRepository.save(appointment);
        }


        return "http://localhost:5173/payment?paymentId=" + paymentId + "&status=success";
    }


    @Transactional
    @Override
    public void confirmPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);

        ServiceAppointment appointment = payment.getAppointment();
        appointment.setStatus(AppointmentStatus.COMPLETED);
        serviceAppointmentRepository.save(appointment);
    }

    @Override
    public Map<String, Object> createVNPayPayment(Long paymentId, String paymentMethod) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Nếu người dùng chọn VNPAY thì cập nhật lại phương thức thanh toán
        if (paymentMethod != null && !"".equals(paymentMethod)) {
            payment.setPaymentMethod(paymentMethod.toUpperCase());
            paymentRepository.save(payment);
        }

        if (!"VNPAY".equalsIgnoreCase(payment.getPaymentMethod())) {
            throw new RuntimeException("Payment method is not VNPAY");
        }

        try {
            String vnpUrl = vnPayService.createVNPayUrl(
                    String.valueOf(payment.getId()),
                    Math.round(payment.getAmount())
            );

            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment);

            Map<String, Object> result = new HashMap<>();
            result.put("paymentId", payment.getId());
            result.put("amount", payment.getAmount());
            result.put("vnpUrl", vnpUrl);
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error creating VNPay URL: " + e.getMessage());
        }
    }

    private Long extractPaymentId(String orderInfo) {
        if (orderInfo == null || !orderInfo.contains(":")) {
            throw new IllegalArgumentException("Invalid order info format");
        }
        return Long.parseLong(orderInfo.split(":")[1].trim());
    }

    @Override
    public Map<String, Object> getPaymentDetailsByAppointment(Long appointmentId) {
        ServiceAppointment appointment = serviceAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Payment payment = paymentRepository.findByAppointmentId(appointmentId);
        if (payment == null) {
            throw new RuntimeException("Payment not found for this appointment");
        }

        ServiceReport report = appointment.getReport();
        double totalLaborCost = 0.0;
        double totalPartCost = 0.0;

        if (report != null) {
            List<ServiceReportDetails> details = serviceReportDetailsRepository.findByReport(report);
            for (ServiceReportDetails detail : details) {
                totalLaborCost += detail.getLaborCost() != null ? detail.getLaborCost() : 0.0;
                totalPartCost += detail.getPartCost() != null ? detail.getPartCost() : 0.0;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("paymentId", payment.getId());
        result.put("appointmentId", appointmentId);
        result.put("totalAmount", payment.getAmount());
        result.put("totalLaborCost", totalLaborCost);
        result.put("totalPartCost", totalPartCost);
        result.put("status", payment.getStatus().name());
        result.put("paymentMethod", payment.getPaymentMethod());
        
        return result;
    }

    @Transactional
    @Override
    public void confirmCashPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentMethod("CASH");
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        ServiceAppointment appointment = payment.getAppointment();
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.IN_PROGRESS);
            serviceAppointmentRepository.save(appointment);
        }
    }
}
