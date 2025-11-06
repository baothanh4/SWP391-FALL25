package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.*;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Enum.*;
import com.example.SWP391_FALL25.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;




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

        // Create ServiceReport if it doesn't exist
        if (appointment.getReport() == null) {
            ServiceReport report = new ServiceReport();
            report.setAppointment(appointment);
            report.setReportDate(java.time.LocalDate.now());
            report.setCurrentKm(0); // Will be updated when technician analyzes kilometer
            reportRepository.save(report);
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

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new RuntimeException("Deatail total cost report must be approved first");
        }

        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Users> getAllTechnicians() {
        return userRepository.findByRole(Role.TECHNICIAN);
    }

    @Override
    public Users getTechnicianById(Long technicianId) {
        return userRepository.findByIdAndRole(technicianId, Role.TECHNICIAN)
                .orElseThrow(() -> new RuntimeException("Technician not found with id: " + technicianId));
    }

    @Override
    public AppointmentDTO getAppointmentById(Long appointmentId) {
        ServiceAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        return mapToDTO(appointment);
    }

    @Override
    public List<PartDTO> getAllParts() {
        List<Part> parts = partRepository.findAll();
        return parts.stream()
                .map(part -> {
                    PartDTO dto = new PartDTO();
                    dto.setId(part.getId());
                    dto.setName(part.getName());
                    dto.setPrice(part.getPrice());
                    dto.setQuantity(part.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<AppointmentDTO> getTechnicianAppointments(String technicianName, Pageable pageable, String search, String status, String sortBy) {
        Specification<ServiceAppointment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Filter by technician name
            predicates.add(criteriaBuilder.equal(root.get("technicianAssigned"), technicianName));
            
            // Search filter - search in customer name, phone, license plate
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicle").get("customer").get("fullname")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicle").get("customer").get("phone")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("vehicle").get("licensePlate")), searchPattern)
                );
                predicates.add(searchPredicate);
            }
            
            // Status filter
            if (status != null && !status.trim().isEmpty()) {
                try {
                    AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("status"), appointmentStatus));
                } catch (IllegalArgumentException e) {
                    // Invalid status, ignore filter
                }
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        // Apply sorting
        Pageable sortedPageable = pageable;
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort;
            switch (sortBy.toLowerCase()) {
                case "date":
                case "date_asc":
                    sort = Sort.by(Sort.Direction.ASC, "appointmentDate", "appointmentTime");
                    break;
                case "date_desc":
                    sort = Sort.by(Sort.Direction.DESC, "appointmentDate", "appointmentTime");
                    break;
                case "status":
                    sort = Sort.by(Sort.Direction.ASC, "status");
                    break;
                default:
                    sort = pageable.getSort();
            }
            sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        
        Page<ServiceAppointment> appointments = appointmentRepository.findAll(spec, sortedPageable);
        List<AppointmentDTO> result = appointments.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(result, sortedPageable, appointments.getTotalElements());
    }

    private AppointmentDTO mapToDTO(ServiceAppointment a) {
        AppointmentDTO dto = new AppointmentDTO();
        
        // Basic appointment fields
        dto.setAppointmentId(a.getId());
        dto.setStatus(a.getStatus().name());
        dto.setAppointmentDate(a.getAppointmentDate());
        dto.setAppointmentTime(a.getAppointmentTime());
        dto.setTechnicianAssigned(a.getTechnicianAssigned());
        
        // Technician detailed information
        if (a.getTechnicianAssigned() != null && !a.getTechnicianAssigned().trim().isEmpty()) {
            userRepository.findByFullnameAndRole(a.getTechnicianAssigned(), Role.TECHNICIAN)
                .ifPresent(technician -> {
                    dto.setTechnicianId(technician.getId());
                    dto.setTechnicianName(technician.getFullname());
                    dto.setTechnicianPhone(technician.getPhone());
                });
        }
        
        // Vehicle information
        if (a.getVehicle() != null) {
            dto.setVehicleId(a.getVehicle().getId());
            dto.setLicensePlate(a.getVehicle().getLicensePlate());
            dto.setVin(a.getVehicle().getVin());
            
            // Combine brand and model for vehicleModel field
            String vehicleModel = "";
            if (a.getVehicle().getBrand() != null) {
                vehicleModel += a.getVehicle().getBrand();
            }
            if (a.getVehicle().getModel() != null) {
                if (!vehicleModel.isEmpty()) {
                    vehicleModel += " ";
                }
                vehicleModel += a.getVehicle().getModel();
            }
            if (a.getVehicle().getYear() != null) {
                if (!vehicleModel.isEmpty()) {
                    vehicleModel += " ";
                }
                vehicleModel += "(" + a.getVehicle().getYear() + ")";
            }
            dto.setVehicleModel(vehicleModel.isEmpty() ? null : vehicleModel);
            
            // Nested vehicle object for maintenance report
            AppointmentDTO.VehicleDTO vehicleDTO = new AppointmentDTO.VehicleDTO();
            vehicleDTO.setId(a.getVehicle().getId());
            vehicleDTO.setVin(a.getVehicle().getVin());
            vehicleDTO.setLicensePlate(a.getVehicle().getLicensePlate());
            vehicleDTO.setBrand(a.getVehicle().getBrand());
            vehicleDTO.setModel(a.getVehicle().getModel());
            vehicleDTO.setYear(a.getVehicle().getYear());
            vehicleDTO.setOdometer(a.getVehicle().getOdometer());
            dto.setVehicle(vehicleDTO);
            
            // Customer information through vehicle
            if (a.getVehicle().getCustomer() != null) {
                dto.setCustomerId(a.getVehicle().getCustomer().getId());
                dto.setCustomerName(a.getVehicle().getCustomer().getFullname());
                dto.setCustomerPhone(a.getVehicle().getCustomer().getPhone());
                dto.setCustomerEmail(a.getVehicle().getCustomer().getEmail());
            }
        }
        
        // Service center information
        if (a.getServiceCenter() != null) {
            dto.setServiceCenterId(a.getServiceCenter().getId());
            dto.setServiceCenterName(a.getServiceCenter().getName());
        }
        
        // Service report information
        if (a.getReport() != null) {
            // Service type information from ServiceReport details
            if (a.getReport().getDetails() != null && !a.getReport().getDetails().isEmpty()) {
                String serviceTypes = a.getReport().getDetails().stream()
                    .map(ServiceReportDetails::getService)
                    .filter(service -> service != null && !service.trim().isEmpty())
                    .distinct()
                    .collect(Collectors.joining(", "));
                
                if (!serviceTypes.isEmpty()) {
                    dto.setServiceType(serviceTypes);
                }
            }
            
            // Nested report object for maintenance report
            AppointmentDTO.ReportDTO reportDTO = new AppointmentDTO.ReportDTO();
            reportDTO.setId(a.getReport().getId());
            reportDTO.setCurrentKm(a.getReport().getCurrentKm());
            reportDTO.setReportDate(a.getReport().getReportDate());
            dto.setReport(reportDTO);
        }
        
        // Payment information
        if (a.getPayment() != null) {
            dto.setPaymentAmount(a.getPayment().getAmount());
            dto.setPaymentStatus(a.getPayment().getStatus() != null ? a.getPayment().getStatus().name() : null);
            dto.setPaymentMethod(a.getPayment().getPaymentMethod());
        }
        
        return dto;
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
