package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Entity.ServiceReport;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Enum.Role;
import com.example.SWP391_FALL25.Repository.ServiceAppointmentRepository;
import com.example.SWP391_FALL25.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private ServiceAppointmentRepository appointmentRepository;
    
    @Autowired
    private UserRepository userRepository;


    @Override
    public Page<AppointmentDTO> getAllAppointments(Pageable pageable, String search, String status, String priority, String sortBy) {
        Specification<ServiceAppointment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
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
            
            // Note: Priority filter is not implemented as the ServiceAppointment entity doesn't have a priority field
            // If you need to add priority, you'll need to add it to the ServiceAppointment entity first
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        // Apply sorting
        Pageable sortedPageable = pageable;
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort;
            switch (sortBy.toLowerCase()) {
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
        List<AppointmentDTO> result = new ArrayList<>();

        for (ServiceAppointment a : appointments.getContent()) {
            AppointmentDTO dto = mapToDTO(a);
            result.add(dto);
        }
        return new PageImpl<>(result, sortedPageable, appointments.getTotalElements());
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        List<ServiceAppointment> appointments = appointmentRepository.findAll();
        List<AppointmentDTO> result = new ArrayList<>();

        for (ServiceAppointment a : appointments) {
            AppointmentDTO dto = mapToDTO(a);
            result.add(dto);
        }
        return result;
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<ServiceAppointment> allAppointments = appointmentRepository.findAll();
        
        long total = allAppointments.size();
        long pending = allAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.PENDING)
                .count();
        long assigned = allAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.ASSIGNED)
                .count();
        long inProgress = allAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.IN_PROGRESS)
                .count();
        long completed = allAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .count();
        
        // Unassigned = appointments with no technician assigned (PENDING status typically)
        long unassigned = allAppointments.stream()
                .filter(a -> a.getTechnicianAssigned() == null || a.getTechnicianAssigned().trim().isEmpty())
                .count();
        
        // Note: Priority is not stored in ServiceAppointment entity, so returning 0
        // If you add a priority field to ServiceAppointment, update these counts
        long highPriority = 0;
        long mediumPriority = 0;
        long lowPriority = 0;
        
        // Frontend expects these field names
        stats.put("total", total);
        stats.put("pending", pending);
        stats.put("assigned", assigned);
        stats.put("inProgress", inProgress);
        stats.put("completed", completed);
        stats.put("unassigned", unassigned);
        stats.put("highPriority", highPriority);
        stats.put("mediumPriority", mediumPriority);
        stats.put("lowPriority", lowPriority);
        
        return stats;
    }

    @Override
    public AppointmentDTO getAppointmentById(Long appointmentId) {
        ServiceAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        
        return mapToDTO(appointment);
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
}
