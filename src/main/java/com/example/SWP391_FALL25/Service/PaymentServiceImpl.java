package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Entity.Payment;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Repository.PaymentRepository;
import com.example.SWP391_FALL25.Repository.ServiceAppointmentRepository;
import com.example.SWP391_FALL25.Utility.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    private static final String VNP_HASH_SECRET="GNPMXK160WDIPNTPV5D5AZ29BLXTHDP7";

    @Transactional
    @Override
    public String handleVnpayReturn(HttpServletRequest request) {
        Map<String, String> vnpParams = VNPayUtils.getVNPayResponseParams(request);
        String vnpSecureHash = vnpParams.get("vnp_SecureHash");

        // ✅ Xác minh chữ ký
        boolean isValid = VNPayUtils.verifySignature(vnpParams, vnpSecureHash, VNP_HASH_SECRET);
        if (!isValid) {
            throw new RuntimeException("Invalid signature");
        }

        // ✅ Kiểm tra mã phản hồi
        String responseCode = vnpParams.get("vnp_ResponseCode");
        if (!"00".equals(responseCode)) {
            throw new RuntimeException("Payment failed with code: " + responseCode);
        }

        // ✅ Lấy paymentId từ vnp_OrderInfo
        String orderInfo = vnpParams.get("vnp_OrderInfo").replace("Thanh toan cho ma GD: ", "").trim();
        Long paymentId = Long.parseLong(orderInfo);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus("COMPLETED");
        paymentRepository.save(payment);

        // ✅ Cập nhật trạng thái của appointment
        ServiceAppointment appointment = payment.getAppointment();
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            serviceAppointmentRepository.save(appointment);
        }

        // ✅ Redirect về React
        return "http://localhost:5173/payment?paymentId=" + paymentId + "&status=success";
    }


    @Transactional
    @Override
    public void confirmPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus("COMPLETED");
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

            payment.setStatus("PENDING");
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
}
