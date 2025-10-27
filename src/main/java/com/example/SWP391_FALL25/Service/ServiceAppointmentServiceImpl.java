package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.ServiceAppointmentDTO;
import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Enum.PaymentStatus;
import com.example.SWP391_FALL25.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceAppointmentServiceImpl implements ServiceAppointmentService{

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private MaintenancePlanRepository maintenancePlanRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private MaintenancePlanItemRepository maintenancePlanItemRepository;

    @Autowired
    private ServiceReportDetailsRepository serviceReportDetailsRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ServiceAppointment createAppointment(Long vehicleId, Long serviceId, ServiceAppointmentDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        ServiceCenter serviceCenter = serviceCenterRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service center not found"));

        ServiceAppointment serviceAppointment = new ServiceAppointment();
        serviceAppointment.setVehicle(vehicle);
        serviceAppointment.setServiceCenter(serviceCenter);
        serviceAppointment.setAppointmentDate(dto.getAppointmentDate());
        serviceAppointment.setAppointmentTime(dto.getAppointmentTime());
        serviceAppointment.setTechnicianAssigned("None");
        serviceAppointment.setStatus(AppointmentStatus.PENDING);

        ServiceAppointment appointment = serviceAppointmentRepository.save(serviceAppointment);

        createReminder(vehicle, dto.getAppointmentDate());

        // ✅ Gửi email thông báo
        try {
            String to = vehicle.getCustomer().getEmail(); // hoặc dto.getEmail() nếu có trong DTO
            String subject = "Xác nhận lịch hẹn dịch vụ xe";
            String body = "Xin chào " + vehicle.getCustomer().getFullname() + ",\n\n"
                    + "Bạn đã đặt lịch hẹn dịch vụ thành công.\n\n"
                    + "📅 Ngày: " + dto.getAppointmentDate() + "\n"
                    + "🕓 Thời gian: " + dto.getAppointmentTime() + "\n"
                    + "🏢 Trung tâm: " + serviceCenter.getName() + "\n"
                    + "🚗 Xe: " + vehicle.getBrand() + " " + vehicle.getModel() + "\n\n"
                    + "Chúng tôi sẽ liên hệ bạn sớm để xác nhận chi tiết.\n\n"
                    + "Trân trọng,\nĐội ngũ Dịch vụ khách hàng.";

            emailService.sendEmail(to, subject, body);
            System.out.println("✅ Email xác nhận đã được gửi đến " + to);
        } catch (Exception e) {
            System.err.println("❌ Không thể gửi email xác nhận: " + e.getMessage());
        }

        return appointment;
    }

    @Override
    public ServiceAppointment assignTechnician(Long appointmentId, Long technicianId) {
        ServiceAppointment appointment = serviceAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Users technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        appointment.setTechnicianAssigned(technician.getFullname());
        appointment.setStatus(AppointmentStatus.ASSIGNED);

        // Nếu chưa có report thì tạo
        if (appointment.getReport() == null) {
            ServiceReport report = new ServiceReport();
            report.setReportDate(LocalDate.now());
            report.setAppointment(appointment);
            reportRepository.save(report);
        }

        return serviceAppointmentRepository.save(appointment);
    }


    @Override
    @Transactional
    public List<ServiceReportDetails> addReportDetails(Long reportId, List<ServiceReportDetailDTO> reportDTO) {
        ServiceReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        ServiceAppointment appointment = report.getAppointment();
        Vehicle vehicle = appointment.getVehicle();

        if (!AppointmentStatus.IN_PROGRESS.equals(appointment.getStatus())) {
            appointment.setStatus(AppointmentStatus.IN_PROGRESS);
            serviceAppointmentRepository.save(appointment);
        }

        Integer currentKm = reportDTO.get(0).getCurrentKm();
        if (currentKm == null) {
            throw new RuntimeException("Current(Km) must be provided");
        }


        MaintenancePlan plan = maintenancePlanRepository
                .findTopByIntervalKmLessThanEqualOrderByIntervalKmDesc(currentKm)
                .orElseThrow(() -> new RuntimeException("No matching maintenance plan found"));

        List<MaintenancePlanItem> items = maintenancePlanItemRepository.findByMaintenancePlan(plan);

        List<ServiceReportDetails> savedDetails = new ArrayList<>();
        for (MaintenancePlanItem item : items) {
            ServiceReportDetails details = new ServiceReportDetails();
            details.setReport(report);
            details.setMaintenancePlanItem(item);
            details.setService(item.getTaskName());
            details.setActionType(null);
            details.setConditionStatus(null);
            details.setLaborCost(0.0);
            details.setPartCost(0.0);
            details.setQuantity(0);


            details.setPart(null);
            details.setTotalCost(0.0);

            savedDetails.add(serviceReportDetailsRepository.save(details));
        }


        updateNextReminder(vehicle, plan);


        updatePaymentForAppointment(reportId);

        return savedDetails;
    }



    @Override
    public List<ServiceAppointment> getAppointmentsByTechnician(String fullName) {
        return serviceAppointmentRepository.findByTechnicianAssigned(fullName);
    }

    @Override
    public ServiceReportDetails updateReportDetails(Long detailsId, ServiceReportDetailDTO dto){
        ServiceReportDetails detail = serviceReportDetailsRepository.findById(detailsId)
                .orElseThrow(() -> new RuntimeException("Detail not found"));

        if (dto.getActionType() != null) {
            detail.setActionType(dto.getActionType());
        }

        if (dto.getConditionStatus() != null) {
            detail.setConditionStatus(dto.getConditionStatus());
        }

        if (dto.getLaborCost() != 0.0 && dto.getLaborCost() > 0) {
            detail.setLaborCost(dto.getLaborCost());
        }

        if (dto.getPartId() != null) {
            Part part = partRepository.findById(dto.getPartId())
                    .orElseThrow(() -> new RuntimeException("Part not found"));
            detail.setPart(part);
        }

        if (dto.getQuantity() != 0 && dto.getQuantity() > 0) {
            int oldQuantity = detail.getQuantity();
            int newQuantity = dto.getQuantity();

            Part part = detail.getPart();
            if (part != null) {
                // Hoàn lại số cũ
                part.setQuantity(part.getQuantity() + oldQuantity);

                if (part.getQuantity() < newQuantity) {
                    throw new RuntimeException("Not enough parts in stock: " + part.getName());
                }

                // Trừ số mới
                part.setQuantity(part.getQuantity() - newQuantity);
                partRepository.save(part);

                detail.setPartCost(part.getPrice() * newQuantity);
            }

            detail.setQuantity(newQuantity);
        }

        // Tính lại tổng chi phí
        double laborCost = detail.getLaborCost() != null ? detail.getLaborCost() : 0.0;
        double partCost = detail.getPartCost() != null ? detail.getPartCost() : 0.0;
        detail.setTotalCost(laborCost + partCost);

        // Lưu thay đổi
        ServiceReportDetails savedDetail = serviceReportDetailsRepository.save(detail);

        updatePaymentForAppointment(detail.getReport().getId());

        return savedDetail;
    }

    @Transactional
    @Override
    public List<ServiceReportDetails> regenerateDetailsByKm(Long reportId, Integer currentKm){
       ServiceReport report=reportRepository.findById(reportId).orElseThrow(()->new RuntimeException("Report not found"));

       ServiceAppointment appointment=report.getAppointment();
       Vehicle vehicle=appointment.getVehicle();

       List<ServiceReportDetails> oldDetails=serviceReportDetailsRepository.findByReport(report);
       for(ServiceReportDetails d:oldDetails){
           Part part=d.getPart();
           if(part!=null && d.getQuantity()>0){
               part.setQuantity(part.getQuantity()+d.getQuantity());
               partRepository.save(part);
           }
           serviceReportDetailsRepository.delete(d);
       }

       MaintenancePlan currentPlan=maintenancePlanRepository.findTopByIntervalKmLessThanEqualOrderByIntervalKmDesc(currentKm).orElseThrow(()->new RuntimeException("No matching maintenance plan not found"));
       List<MaintenancePlanItem> items=maintenancePlanItemRepository.findByMaintenancePlan(currentPlan);

       List<ServiceReportDetails> savedDetails=new ArrayList<>();
       for(MaintenancePlanItem item:items){
           ServiceReportDetails details=new ServiceReportDetails();
           details.setReport(report);
           details.setMaintenancePlanItem(item);
           details.setService(item.getTaskName());
           details.setActionType(null);
           details.setConditionStatus(null);
           details.setLaborCost(0.0);
           details.setPartCost(0.0);
           details.setQuantity(0);
           details.setPart(null);
           details.setTotalCost(0.0);

           savedDetails.add(serviceReportDetailsRepository.save(details));
       }

       List<MaintenancePlan> allPlans=maintenancePlanRepository.findAll();
       for(MaintenancePlan plan:allPlans){
           if(plan.getIntervalKm()<currentPlan.getIntervalKm()){
               boolean exists=reminderRepository.existsByVehicleAndMaintenancePlan(vehicle,plan);
               if(!exists){
                   Reminder expiredReminder=new Reminder();
                   expiredReminder.setVehicle(vehicle);
                   expiredReminder.setMaintenancePlan(plan);
                   expiredReminder.setStatus("EXPIRED");
                   expiredReminder.setReminderDate(LocalDate.now().minusMonths(plan.getIntervalKm()));
                   reminderRepository.save(expiredReminder);
               }
           }
       }

       List<Reminder> reminders=reminderRepository.findByVehicle(vehicle);
       for(Reminder reminder:reminders){
           if(reminder.getMaintenancePlan().getIntervalKm()<currentPlan.getIntervalKm() && !"COMPLETED".equalsIgnoreCase(reminder.getStatus())){
               reminder.setStatus("EXPIRED");
               reminderRepository.save(reminder);
           }
       }

       updateReminderForCurrentKm(vehicle, currentPlan);

       updatePaymentForAppointment(reportId);

       return savedDetails;
    }


    private void updatePaymentForAppointment(Long reportId) {
        Double totalCost = serviceReportDetailsRepository.calculateTotalByReportId(reportId);
        if (totalCost == null) totalCost = 0.0;

        ServiceReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        ServiceAppointment appointment = report.getAppointment();

        Payment payment = paymentRepository.findByAppointmentId(appointment.getId());
        if (payment == null) {
            payment = new Payment();
            payment.setAppointment(appointment);
            payment.setAmount(totalCost);
            payment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);
        } else {
            payment.setAmount(totalCost);
            paymentRepository.save(payment);
        }
    }

    private void updateNextReminder(Vehicle vehicle,MaintenancePlan currentPlan){
        Integer nextKm=currentPlan.getIntervalKm()+5000;
        MaintenancePlan nextPlan=maintenancePlanRepository.findTopByIntervalKmGreaterThanOrderByIntervalKmAsc(currentPlan.getIntervalKm()).orElse(null);

        if(nextPlan!=null){
            Reminder reminder=new Reminder();
            reminder.setVehicle(vehicle);
            reminder.setMaintenancePlan(nextPlan);
            reminder.setReminderDate(LocalDate.now().plusMonths(nextPlan.getIntervalMonths()));
            reminder.setStatus("PENDING");
            reminderRepository.save(reminder);
        }
    }

    private void createReminder(Vehicle vehicle, LocalDate appointmentDate){
        MaintenancePlan plan=maintenancePlanRepository.findByIntervalMonths(6).orElseThrow(()->new RuntimeException("Maintenance plan not found"));
        Reminder reminder=new Reminder();
        reminder.setVehicle(vehicle);
        reminder.setMaintenancePlan(plan);
        reminder.setStatus("Pending");
        reminder.setReminderDate(appointmentDate.plusMonths(plan.getIntervalMonths()));

        reminderRepository.save(reminder);
    }

    private void updateReminderForCurrentKm(Vehicle vehicle,MaintenancePlan currentPlan){
        List<Reminder> reminders=reminderRepository.findByVehicle(vehicle);

        for(Reminder reminder:reminders){
            MaintenancePlan plan=reminder.getMaintenancePlan();
            if(plan==null){
                continue;
            }

            if(plan.getIntervalKm()<currentPlan.getIntervalKm()){
                reminder.setStatus("EXPIRED");
            }else if(plan.getIntervalKm().equals(currentPlan.getIntervalKm())){
                reminder.setStatus("ACTIVE");
            }else{
                reminder.setStatus("PENDING");
            }
            reminderRepository.save(reminder);
        }

        MaintenancePlan nextPlan=maintenancePlanRepository.findTopByIntervalKmGreaterThanOrderByIntervalKmAsc(currentPlan.getIntervalKm()).orElse(null);

        if(nextPlan!=null && reminders.stream().noneMatch(r ->r.getMaintenancePlan().getId().equals(nextPlan.getId()))){
            Reminder nextReminder=new Reminder();
            nextReminder.setVehicle(vehicle);
            nextReminder.setMaintenancePlan(nextPlan);
            nextReminder.setStatus("PENDING");
            nextReminder.setReminderDate(LocalDate.now().plusMonths(nextPlan.getIntervalMonths()));
            reminderRepository.save(nextReminder);
        }
    }
}
