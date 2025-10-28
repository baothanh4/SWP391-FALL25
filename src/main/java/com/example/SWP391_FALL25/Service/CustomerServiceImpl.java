package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.*;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Repository.*;
import com.example.SWP391_FALL25.Enum.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ServiceReportDetailsRepository serviceReportDetailsRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReminderRepository reminderRepository;


    @Override
    public Vehicle addCar(Long customerId, VehicleDTO vehicleDTO) {
        String vin = vehicleDTO.getVin();
        if (vin == null || vin.trim().isEmpty()) {
            throw new RuntimeException("VIN must not be empty.");
        }


        vin = vin.trim().toUpperCase();


        if (!vin.matches("^[A-HJ-NPR-Z0-9]{17}$")) {
            throw new RuntimeException("Invalid VIN format. VIN must have 17 characters (letters and numbers).");
        }


        Optional<Vehicle> optionalVehicle = vehicleRepository.findByVin(vin);


        Users customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found."));

        if (optionalVehicle.isPresent()) {
            Vehicle existingVehicle = optionalVehicle.get();

            // Nếu xe đã có chủ khác
            if (existingVehicle.getCustomer() != null &&
                    !existingVehicle.getCustomer().getId().equals(customerId)) {
                throw new RuntimeException("This vehicle (VIN) already belongs to another customer.");
            }

            // Nếu xe chưa có chủ thì gán chủ
            if (existingVehicle.getCustomer() == null) {
                existingVehicle.setCustomer(customer);
                return vehicleRepository.save(existingVehicle);
            }

            // Nếu xe đã thuộc customer này rồi
            return existingVehicle;
        }

        // Nếu VIN chưa tồn tại trong hệ thống → tạo xe mới
        Vehicle newVehicle = new Vehicle();
        newVehicle.setVin(vin);
        newVehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        newVehicle.setBrand(vehicleDTO.getBrand());
        newVehicle.setModel(vehicleDTO.getModel());
        newVehicle.setOdometer(vehicleDTO.getOdometer());
        newVehicle.setYear(vehicleDTO.getYear());
        newVehicle.setCustomer(customer);

        return vehicleRepository.save(newVehicle);
    }


    @Override
    public Users updateInformation(Long customerId, RegisterRequest request){
        Users users=userRepository.findById(customerId).orElseThrow(()->new RuntimeException("Customer not found"));


        if(request.getFullname()!=null && !request.getFullname().isEmpty()) {
            users.setFullname(request.getFullname());
        }
        if(request.getEmail()!=null && !request.getEmail().isEmpty()){
            users.setEmail(request.getEmail());
        }
        if(request.getAddress()!=null && !request.getAddress().isEmpty()){
            users.setAddress(request.getAddress());
        }
        if(request.getDob()!=null && !request.getDob().toString().isEmpty()){
            users.setDob(request.getDob());
        }
        if(request.getPassword()!=null && !request.getPassword().isEmpty()){
            users.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userRepository.save(users);
    }

    @Override
    public void deleteCar(Long vehicleId){
        Vehicle vehicle=vehicleRepository.findById(vehicleId).orElseThrow(()->new RuntimeException("Vehicle not found"));

        vehicleRepository.delete(vehicle);
    }

    @Override
    public VehicleDTO getVehicleById(Long id){
        Vehicle vehicle=vehicleRepository.findById(id).orElseThrow(()->new RuntimeException("Vehicle not found"));

        VehicleDTO vehicleDTO=new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setVin(vehicle.getVin());
        vehicleDTO.setLicensePlate(vehicle.getLicensePlate());
        vehicleDTO.setBrand(vehicle.getBrand());
        vehicleDTO.setModel(vehicle.getModel());
        vehicleDTO.setOdometer(vehicle.getOdometer());
        vehicleDTO.setYear(vehicle.getYear());

        return vehicleDTO;
    }

    @Override
    public List<ServiceAppointment> getAppointmentByUser(Long userId){
        return serviceAppointmentRepository.findByUserId(userId);
    }

    @Override
    public DetailTotalCostResponseDTO getDetailTotalCostReport(Long appointmentId) {
        ServiceAppointment appointment = serviceAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getReport() == null) {
            throw new RuntimeException("No report found");
        }

        ServiceReport report = appointment.getReport();
        List<ServiceReportDetails> details = report.getDetails();

        List<DetailTotalCostItemDTO> items = new ArrayList<>();
        double totalLabor = 0;
        double totalPart = 0;

        for (ServiceReportDetails detail : details) {
            DetailTotalCostItemDTO item = new DetailTotalCostItemDTO();
            item.setDetailId(detail.getId());
            item.setService(detail.getService());
            item.setActionType(detail.getActionType());
            item.setConditionStatus(detail.getConditionStatus());
            item.setPartName(detail.getPart() != null ? detail.getPart().getName() : "N/A");
            item.setPartPrice(detail.getPartCost());
            item.setLaborCost(detail.getLaborCost());
            item.setSubtotal(detail.getTotalCost());

            items.add(item);
            totalLabor += detail.getLaborCost();
            totalPart += detail.getPartCost();
        }

        DetailTotalCostResponseDTO response = new DetailTotalCostResponseDTO();
        response.setAppointmentId(appointmentId);
        response.setReportId(report.getId());
        response.setInspectionDate(report.getReportDate());
        response.setVehicleInfo(appointment.getVehicle().getBrand() + " " +
                appointment.getVehicle().getModel());
        response.setItems(items);
        response.setTotalLaborCost(totalLabor);
        response.setTotalPartCost(totalPart);
        response.setGrandTotal(totalLabor + totalPart);
        response.setStatus(appointment.getStatus().name());

        return response;
    }

    @Transactional
    @Override
    public ServiceAppointment approveDetailTotalCostReport(Long appointmentId, String paymentMethod) {
        ServiceAppointment appointment = serviceAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.QUOTATION_SENT) {
            throw new RuntimeException("Quotation not sent yet");
        }

        // Cập nhật payment method
        Payment payment = paymentRepository.findByAppointmentId(appointmentId);
        if (payment != null) {
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus(PaymentStatus.PENDING); // Chuyển từ QUOTATION sang PENDING
            paymentRepository.save(payment);
        }

        // Cập nhật trạng thái
        appointment.setStatus(AppointmentStatus.APPROVED);
        serviceAppointmentRepository.save(appointment);

        // Gửi email xác nhận
        sendApprovalEmail(appointment);

        return appointment;
    }

    @Transactional
    @Override
    public ServiceAppointment rejectDetailTotalCostReport(Long appointmentId, String reason) {
        ServiceAppointment appointment = serviceAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if("ASSIGNED".equalsIgnoreCase(appointment.getStatus().name())){
            throw new RuntimeException("Cannot cancel appointment because status is ASSIGNED");
        }

        if(!"PENDING".equalsIgnoreCase(appointment.getStatus().name())){
            throw new RuntimeException("Appointment cannot be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        serviceAppointmentRepository.save(appointment);

        ServiceReport report=appointment.getReport();
        if(report!=null){
            List<ServiceReportDetails> details=serviceReportDetailsRepository.findByReport(report);
            for(ServiceReportDetails d:details){
                if(d.getPart()!=null && d.getQuantity()>0){
                    Part part = d.getPart();
                    part.setQuantity(part.getQuantity()+d.getQuantity());
                    partRepository.save(part);
                }
                serviceReportDetailsRepository.delete(d);
            }
            reportRepository.delete(report);
        }

        Payment payment=paymentRepository.findByAppointmentId(appointmentId);
        if(payment!=null){
            paymentRepository.delete(payment);
        }

        List<Reminder> reminders=reminderRepository.findByVehicle(appointment.getVehicle());
        for(Reminder reminder:reminders){
            if(!"DONE".equalsIgnoreCase(reminder.getStatus())){
                reminderRepository.delete(reminder);
            }
        }
        if(appointment.getTechnicianAssigned()!=null){
            appointment.setTechnicianAssigned(null);
        }

        sendRejectionEmail(appointment, reason);

        serviceAppointmentRepository.save(appointment);
        return appointment;
    }




    private void sendApprovalEmail(ServiceAppointment appointment) {
        try {
            String to = appointment.getVehicle().getCustomer().getEmail();
            String subject = "Xác nhận duyệt báo giá";
            String body = String.format(
                    "Kính gửi %s,\n\n" +
                            "Cảm ơn bạn đã xác nhận báo giá. Chúng tôi sẽ bắt đầu tiến hành sửa chữa xe của bạn.\n\n" +
                            "Trân trọng,\nĐội ngũ kỹ thuật",
                    appointment.getVehicle().getCustomer().getFullname()
            );
            emailService.sendEmail(to, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send approval email: " + e.getMessage());
        }
    }

    private void sendRejectionEmail(ServiceAppointment appointment, String reason) {
        try {
            String to = appointment.getVehicle().getCustomer().getEmail();
            String subject = "Thông báo hủy dịch vụ";
            String body = String.format(
                    "Kính gửi %s,\n\n" +
                            "Chúng tôi đã nhận được yêu cầu hủy dịch vụ của bạn.\n" +
                            "Lý do: %s\n\n" +
                            "Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ.\n\n" +
                            "Trân trọng,\nĐội ngũ CSKH",
                    appointment.getVehicle().getCustomer().getFullname(),
                    reason
            );
            emailService.sendEmail(to, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send rejection email: " + e.getMessage());
        }
    }

}
