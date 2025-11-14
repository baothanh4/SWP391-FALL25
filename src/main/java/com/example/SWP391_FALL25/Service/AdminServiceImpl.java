package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.ServiceCenterDTO;
import com.example.SWP391_FALL25.DTO.Auth.DashboardStatsDTO;
import com.example.SWP391_FALL25.DTO.Auth.MaintenancePlanDTO;
import com.example.SWP391_FALL25.DTO.Auth.PartTypeDTO;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Enum.PaymentStatus;
import com.example.SWP391_FALL25.Enum.Role;
import com.example.SWP391_FALL25.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartTypeRepository partTypeRepository;

    @Autowired
    private MaintenancePlanRepository maintenancePlanRepository;

    @Autowired
    private MaintenancePlanItemRepository maintenancePlanItemRepository;

    @Autowired
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    public Page<Users> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public Users updateUser(Long id, RegisterRequest request) {
        Users user = getUserById(id);

        if (request.getFullname() != null && !request.getFullname().isEmpty()) {
            user.setFullname(request.getFullname());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            user.setAddress(request.getAddress());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Users user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public List<Users> getUsersByRole(String role) {
        Role userRole = Role.valueOf(role.toUpperCase());
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == userRole)
                .collect(Collectors.toList());
    }



    @Override
    public List<ServiceCenter> getAllServiceCenters() {
        return serviceCenterRepository.findAll();
    }

    @Override
    public ServiceCenter getServiceCenterById(Long id) {
        return serviceCenterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service center not found with id: " + id));
    }

    @Override
    @Transactional
    public ServiceCenter createServiceCenter(ServiceCenterDTO dto) {
        ServiceCenter serviceCenter = new ServiceCenter();
        serviceCenter.setName(dto.getName());
        serviceCenter.setLocation(dto.getLocation());
        serviceCenter.setContactNumber(dto.getContactNumber());
        return serviceCenterRepository.save(serviceCenter);
    }

    @Override
    @Transactional
    public ServiceCenter updateServiceCenter(Long id, ServiceCenterDTO dto) {
        ServiceCenter serviceCenter = getServiceCenterById(id);

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            serviceCenter.setName(dto.getName());
        }
        if (dto.getLocation() != null && !dto.getLocation().isEmpty()) {
            serviceCenter.setLocation(dto.getLocation());
        }
        if (dto.getContactNumber() != null && !dto.getContactNumber().isEmpty()) {
            serviceCenter.setContactNumber(dto.getContactNumber());
        }

        return serviceCenterRepository.save(serviceCenter);
    }

    @Override
    @Transactional
    public void deleteServiceCenter(Long id) {
        ServiceCenter serviceCenter = getServiceCenterById(id);
        serviceCenterRepository.delete(serviceCenter);
    }



    @Override
    public Page<Part> getAllParts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return partRepository.findAll(pageable);
    }

    @Override
    public Part getPartById(Long id) {
        return partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + id));
    }

    @Override
    @Transactional
    public Part createPart(PartDTO dto, Long partTypeId) {
        PartType partType = partTypeRepository.findById(partTypeId)
                .orElseThrow(() -> new RuntimeException("Part type not found"));

        Part part = new Part();
        part.setName(dto.getName());
        part.setPrice(dto.getPrice());
        part.setQuantity(dto.getQuantity());
        part.setPartType(partType);

        return partRepository.save(part);
    }

    @Override
    @Transactional
    public Part updatePartAdmin(Long id, PartDTO dto) {
        Part part = getPartById(id);

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            part.setName(dto.getName());
        }
        if (dto.getPrice() != 0.0) {
            part.setPrice(dto.getPrice());
        }
        if (dto.getQuantity() != 0) {
            part.setQuantity(dto.getQuantity());
        }

        return partRepository.save(part);
    }

    @Override
    @Transactional
    public void deletePart(Long id) {
        Part part = getPartById(id);
        partRepository.delete(part);
    }

    @Override
    public List<Part> getLowStockParts(int threshold) {
        return partRepository.findAll().stream()
                .filter(part -> part.getQuantity() != null && part.getQuantity() <= threshold)
                .collect(Collectors.toList());
    }



    @Override
    public List<PartType> getAllPartTypes() {
        return partTypeRepository.findAll();
    }

    @Override
    @Transactional
    public PartType createPartType(PartTypeDTO dto) {
        PartType partType = new PartType();
        partType.setName(dto.getName());
        partType.setPartNumber(dto.getPartNumber());
        return partTypeRepository.save(partType);
    }

    @Override
    @Transactional
    public PartType updatePartType(Long id, PartTypeDTO dto) {
        PartType partType = partTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part type not found"));

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            partType.setName(dto.getName());
        }
        if (dto.getPartNumber() != null && !dto.getPartNumber().isEmpty()) {
            partType.setPartNumber(dto.getPartNumber());
        }

        return partTypeRepository.save(partType);
    }

    @Override
    @Transactional
    public void deletePartType(Long id) {
        PartType partType = partTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part type not found"));
        partTypeRepository.delete(partType);
    }

    @Override
    public List<SystemLog> getAllSystemLog(){
        return systemLogRepository.findAll();
    }

    @Override
    public Optional<SystemLog> getSystemLogById(Long id){
        return systemLogRepository.findById(id);
    }

    @Override
    public List<MaintenancePlan> getAllMaintenancePlans() {
        return maintenancePlanRepository.findAll();
    }

    @Override
    public MaintenancePlan getMaintenancePlanById(Long id) {
        return maintenancePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance plan not found"));
    }

    @Override
    @Transactional
    public MaintenancePlan createMaintenancePlan(MaintenancePlanDTO dto) {
        MaintenancePlan plan = new MaintenancePlan();
        plan.setIntervalKm(dto.getIntervalKm());
        plan.setIntervalMonths(dto.getIntervalMonths());

        MaintenancePlan savedPlan = maintenancePlanRepository.save(plan);

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            List<MaintenancePlanItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        MaintenancePlanItem item = new MaintenancePlanItem();
                        item.setTaskName(itemDto.getTaskName());
                        item.setPartType(itemDto.getPartType());
                        item.setMaintenancePlan(savedPlan);
                        return item;
                    })
                    .collect(Collectors.toList());
            maintenancePlanItemRepository.saveAll(items);
            savedPlan.setItems(items);
        }

        return savedPlan;
    }

    @Override
    public Double getTotalCost() {
        return paymentRepository.getTotalCost();
    }

    @Override
    @Transactional
    public MaintenancePlan updateMaintenancePlan(Long id, MaintenancePlanDTO dto) {
        MaintenancePlan plan = getMaintenancePlanById(id);

        if (dto.getIntervalKm() != null) {
            plan.setIntervalKm(dto.getIntervalKm());
        }
        if (dto.getIntervalMonths() != null) {
            plan.setIntervalMonths(dto.getIntervalMonths());
        }

        return maintenancePlanRepository.save(plan);
    }

    @Override
    @Transactional
    public void deleteMaintenancePlan(Long id) {
        MaintenancePlan plan = getMaintenancePlanById(id);
        maintenancePlanRepository.delete(plan);
    }

    @Override
    public Page<ServiceAppointment> getAllAppointments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceAppointmentRepository.findAll(pageable);
    }

    @Override
    public List<ServiceAppointment> getAppointmentsByStatus(String status) {
        AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
        return serviceAppointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getStatus() == appointmentStatus)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        ServiceAppointment appointment = serviceAppointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        serviceAppointmentRepository.delete(appointment);
    }

    @Override
    public Page<Payment> getAllPayments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return paymentRepository.findAll(pageable);
    }

    @Override
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }




    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // Count users by role
        stats.setTotalCustomers(getUsersByRole("CUSTOMER").size());
        stats.setTotalStaff(getUsersByRole("STAFF").size());
        stats.setTotalTechnicians(getUsersByRole("TECHNICIAN").size());

        // Count appointments
        stats.setTotalAppointments(serviceAppointmentRepository.count());
        stats.setPendingAppointments(getAppointmentsByStatus("PENDING").size());
        stats.setCompletedAppointments(getAppointmentsByStatus("COMPLETED").size());

        // Revenue
        List<Payment> completedPayments = getPaymentsByStatus(PaymentStatus.COMPLETED);
        double totalRevenue = completedPayments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        stats.setTotalRevenue(totalRevenue);

        // Parts
        stats.setTotalParts(partRepository.count());
        stats.setLowStockParts(getLowStockParts(10).size());

        return stats;
    }


    @Override
    public Double getTotalRevenue(String startDate, String endDate) {
        List<Payment> payments = getPaymentsByStatus(PaymentStatus.COMPLETED);

        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            payments = payments.stream()
                    .filter(payment -> {
                        ServiceAppointment appointment = payment.getAppointment();
                        if (appointment != null && appointment.getAppointmentDate() != null) {
                            LocalDate date = appointment.getAppointmentDate();
                            return !date.isBefore(start) && !date.isAfter(end);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        return payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }


    @Override
    public Map<Integer, Long> getAppointmentsByMonth(int year) {
        Map<Integer, Long> monthlyStats = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            monthlyStats.put(i, 0L);
        }

        List<ServiceAppointment> appointments = serviceAppointmentRepository.findAll();

        appointments.stream()
                .filter(appointment -> appointment.getAppointmentDate() != null
                        && appointment.getAppointmentDate().getYear() == year)
                .forEach(appointment -> {
                    int month = appointment.getAppointmentDate().getMonthValue();
                    monthlyStats.put(month, monthlyStats.get(month) + 1);
                });

        return monthlyStats;
    }

    @Override
    public void unlockUser(Long id){
        Users user=userRepository.findById(id).orElseThrow(() -> new RuntimeException("User id not found"));

        user.setAccountLocked(false);
        user.setFailAttempts(0);
        user.setLockTime(null);
        userRepository.save(user);
    }

}