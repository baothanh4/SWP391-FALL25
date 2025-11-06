package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Entity.Payment;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Enum.PaymentStatus;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private QRService qrService;
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

        // ✅ Lấy paymentId từ thông tin order
        String orderInfo = vnpParams.get("vnp_OrderInfo").replace("Thanh toan cho ma GD: ", "").trim();
        Long paymentId = Long.parseLong(orderInfo);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        // ✅ Cập nhật trạng thái thanh toán
        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);


        ServiceAppointment appointment = payment.getAppointment();
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            serviceAppointmentRepository.save(appointment);
        }


        try {
            System.out.println("📧 appointment: " + payment.getAppointment());
            System.out.println("📧 vehicle: " + (payment.getAppointment() != null ? payment.getAppointment().getVehicle() : null));
            System.out.println("📧 customer: " +
                    (payment.getAppointment() != null && payment.getAppointment().getVehicle() != null
                            ? payment.getAppointment().getVehicle().getCustomer()
                            : null));
            System.out.println("📧 email: " +
                    (payment.getAppointment() != null && payment.getAppointment().getVehicle() != null
                            ? payment.getAppointment().getVehicle().getCustomer().getEmail()
                            : null));
            String to = payment.getAppointment().getVehicle().getCustomer().getEmail();
            String subject = "Xác nhận thanh toán thành công - Genetix";
            String body = String.format(
                    "Xin chào %s,\n\nThanh toán của bạn đã được thực hiện thành công!\n\n" +
                            "Mã giao dịch: %s\nSố tiền: %s VND\nThời gian: %s\n\n" +
                            "Cảm ơn bạn đã sử dụng dịch vụ của Genetix.\n\nTrân trọng,\nĐội ngũ Genetix",
                    payment.getAppointment().getVehicle().getCustomer().getFullname(),
                    paymentId,
                    vnpParams.get("vnp_Amount"),
                    vnpParams.get("vnp_PayDate")
            );
            emailService.sendEmail(to, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Trả về URL redirect về frontend
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

        // ✅ Cập nhật phương thức thanh toán nếu có chỉ định
        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            payment.setPaymentMethod(paymentMethod.toUpperCase());
            paymentRepository.save(payment);
        }

        Map<String, Object> result = new HashMap<>();

        try {
            // ✅ Trường hợp 1: Thanh toán bằng VNPay
            if ("VNPAY".equalsIgnoreCase(payment.getPaymentMethod())) {
                String vnpUrl = vnPayService.createVNPayUrl(
                        String.valueOf(payment.getId()),
                        Math.round(payment.getAmount())
                );

                // Cập nhật trạng thái chờ thanh toán
                payment.setStatus(PaymentStatus.PENDING);
                paymentRepository.save(payment);

                result.put("paymentId", payment.getId());
                result.put("amount", payment.getAmount());
                result.put("paymentMethod", "VNPAY");
                result.put("vnpUrl", vnpUrl);
                result.put("message", "VNPay URL created successfully");
            }

            // ✅ Trường hợp 2: Thanh toán bằng QR ngân hàng
            else if ("QR".equalsIgnoreCase(payment.getPaymentMethod())) {
                // Sinh QR code VietQR
                String qrUrl = qrService.generateBankQr(
                        payment.getAmount(),
                        "Thanh toan dich vu EV #" + payment.getId()
                );

                payment.setStatus(PaymentStatus.PENDING);
                paymentRepository.save(payment);

                result.put("paymentId", payment.getId());
                result.put("amount", payment.getAmount());
                result.put("paymentMethod", "QR");
                result.put("qrUrl", qrUrl);
                result.put("message", "QR payment generated successfully");
            }

            // ❌ Trường hợp không hợp lệ
            else {
                throw new RuntimeException("Unsupported payment method: " + payment.getPaymentMethod());
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error creating payment: " + e.getMessage());
        }
    }


    private Long extractPaymentId(String orderInfo) {
        if (orderInfo == null || !orderInfo.contains(":")) {
            throw new IllegalArgumentException("Invalid order info format");
        }
        return Long.parseLong(orderInfo.split(":")[1].trim());
    }
}
