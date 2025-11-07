package com.example.SWP391_FALL25.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("genetix.noreply@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    public void sendApprovalEmailToTechnician(String technicianEmail, Long appointmentId) {
        if (technicianEmail == null) return;

        String subject = "Khách hàng đã duyệt báo cáo dịch vụ #" + appointmentId;
        String body = String.format(
                "Kính gửi kỹ thuật viên,\n\n" +
                        "Khách hàng đã duyệt báo cáo cho lịch hẹn #%d.\n" +
                        "Vui lòng chuẩn bị hoàn tất quy trình hoặc cập nhật tình trạng.\n\n" +
                        "Trân trọng,\nĐội ngũ Dịch vụ Xe Điện.",
                appointmentId
        );

        sendEmail(technicianEmail, subject, body);
    }

    public void sendRejectionEmailToTechnician(String technicianEmail, Long appointmentId, String feedback) {
        if (technicianEmail == null) return;

        String subject = "Khách hàng từ chối báo cáo dịch vụ #" + appointmentId;
        String body = String.format(
                "Kính gửi kỹ thuật viên,\n\n" +
                        "Khách hàng đã từ chối báo cáo cho lịch hẹn #%d.\n" +
                        "Phản hồi của khách hàng: %s\n\n" +
                        "Vui lòng kiểm tra và chỉnh sửa lại báo cáo nếu cần.\n\n" +
                        "Trân trọng,\nĐội ngũ Dịch vụ Xe Điện.",
                appointmentId, feedback
        );

        sendEmail(technicianEmail, subject, body);
    }
}
