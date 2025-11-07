package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.*;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Enum.*;
import com.example.SWP391_FALL25.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service

public class TechnicianServiceImpl implements TechnicianService{

    @Autowired
    private ServiceAppointmentRepository appointmentRepository;

    @Autowired
    private ServiceReportDetailsRepository detailsRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private MaintenancePlanItemRepository itemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SystemLogService systemLogService;

    @Autowired
    private UserService userService;

    @Transactional
    public Users updateTechnicianProfile(Long technicianId, UpdateUserProfileRequest request) {
        Users technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        if (technician.getRole() != Role.TECHNICIAN) {
            throw new RuntimeException("User is not a technician");
        }

        userService.updateProfile(technician.getId(), request);
        systemLogService.log(technician.getId(), "UPDATE TECHNICIAN PROFILE");

        return userRepository.save(technician);
    }


    @Override

    public Part updatePart(Long partId, PartDTO dto){
        Part part=partRepository.findById(partId).orElseThrow(()->new IllegalArgumentException("Part not found"));

        if(dto.getName()!=null && !dto.getName().isEmpty()){
            part.setName(dto.getName());
        }
        if(dto.getPrice()!=0.0){
            part.setPrice(dto.getPrice());
        }
        if(dto.getQuantity()!=0){
            part.setQuantity(part.getQuantity()- dto.getQuantity());
        }
        return partRepository.save(part);
    }

    @Transactional
    public ServiceAppointment startInspection(Long appointmentId) {
        ServiceAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.ASSIGNED) {
            throw new RuntimeException("Appointment must be ASSIGNED first");
        }

        appointment.setStatus(AppointmentStatus.INSPECTING);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public List<ServiceReportDetails> createDetailTotalCostReport(
            Long reportId,
            List<ServiceReportDetailDTO> detailTotalCostItems) {

        ServiceReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        ServiceAppointment appointment = report.getAppointment();

        if (appointment.getStatus() != AppointmentStatus.INSPECTING) {
            throw new RuntimeException("Must be in INSPECTING status");
        }

        List<ServiceReportDetails> savedDetails = new ArrayList<>();

        for (ServiceReportDetailDTO dto : detailTotalCostItems) {
            Part part = (dto.getPartId() != null)
                    ? partRepository.findById(dto.getPartId()).orElse(null)
                    : null;

            MaintenancePlanItem item = (dto.getMaintenanceItemId() != null)
                    ? itemRepository.findById(dto.getMaintenanceItemId()).orElse(null)
                    : null;

            ServiceReportDetails details = new ServiceReportDetails();
            details.setReport(report);
            details.setPart(part);
            details.setMaintenancePlanItem(item);
            details.setService(dto.getService());
            details.setActionType(dto.getActionType());
            details.setConditionStatus(dto.getConditionStatus());
            details.setLaborCost(dto.getLaborCost());
            details.setPartCost(part != null ? part.getPrice() : 0.0);
            details.setTotalCost(details.getLaborCost() + details.getPartCost());

            savedDetails.add(detailsRepository.save(details));
        }

        // Tính tổng chi phí và tạo Payment với status QUOTATION
        Double totalCost = savedDetails.stream()
                .mapToDouble(ServiceReportDetails::getTotalCost)
                .sum();

        Payment payment = paymentRepository.findByAppointmentId(appointment.getId());
        if (payment == null) {
            payment = new Payment();
            payment.setAppointment(appointment);
        }
        payment.setAmount(totalCost);
        payment.setStatus(PaymentStatus.QUOTATION); // Trạng thái báo giá
        payment.setPaymentMethod(null); // Chưa chọn phương thức
        paymentRepository.save(payment);

        // Cập nhật trạng thái appointment
        appointment.setStatus(AppointmentStatus.QUOTATION_SENT);
        appointmentRepository.save(appointment);

        // Gửi email thông báo cho khách hàng
        sendDetailTotalCostEmail(appointment, totalCost);

        return savedDetails;
    }


    @Transactional
    public ServiceAppointment startRepair(Long appointmentId) {
        ServiceAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.ASSIGNED) {
            throw new RuntimeException("Deatail total cost report must be approved first");
        }

        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        return appointmentRepository.save(appointment);
    }

    private void sendDetailTotalCostEmail(ServiceAppointment appointment, Double totalCost) {
        try {
            String to = appointment.getVehicle().getCustomer().getEmail();
            String subject = "Báo giá sửa chữa xe - " + appointment.getVehicle().getLicensePlate();
            String body = String.format(
                    "Kính gửi %s,\n\n" +
                            "Sau khi kiểm tra xe %s %s (biển số: %s), chúng tôi xin gửi báo giá chi tiết:\n\n" +
                            "💰 Tổng chi phí dự kiến: %,.0f VND\n\n" +
                            "Vui lòng đăng nhập hệ thống để xem chi tiết báo giá và xác nhận.\n\n" +
                            "Trân trọng,\nĐội ngũ kỹ thuật",
                    appointment.getVehicle().getCustomer().getFullname(),
                    appointment.getVehicle().getBrand(),
                    appointment.getVehicle().getModel(),
                    appointment.getVehicle().getLicensePlate(),
                    totalCost
            );
            emailService.sendEmail(to, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send quotation email: " + e.getMessage());
        }
    }




}
