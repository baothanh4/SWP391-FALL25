package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.ServiceCenterDTO;
import com.example.SWP391_FALL25.DTO.Auth.DashboardStatsDTO;
import com.example.SWP391_FALL25.DTO.Auth.PartTypeDTO;
import com.example.SWP391_FALL25.DTO.Auth.MaintenancePlanDTO;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Enum.PaymentStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminService {

    // User Management
    List<Users> getAllUsers();
    Users getUserById(Long id);
    Users updateUser(Long id, RegisterRequest request);
    void deleteUser(Long id);
    List<Users> getUsersByRole(String role);

    // Service Center Management
    List<ServiceCenter> getAllServiceCenters();
    ServiceCenter getServiceCenterById(Long id);
    ServiceCenter createServiceCenter(ServiceCenterDTO dto);
    ServiceCenter updateServiceCenter(Long id, ServiceCenterDTO dto);
    void deleteServiceCenter(Long id);

    // Parts Management
    List<Part> getAllParts();
    Part getPartById(Long id);
    Part createPart(PartDTO dto, Long partTypeId);
    Part updatePartAdmin(Long id, PartDTO dto);
    void deletePart(Long id);
    List<Part> getLowStockParts(int threshold);

    // Part Type Management
    List<PartType> getAllPartTypes();
    PartType createPartType(PartTypeDTO dto);
    PartType updatePartType(Long id, PartTypeDTO dto);
    void deletePartType(Long id);

    List<SystemLog> getAllSystemLog();

    Optional<SystemLog> getSystemLogById(Long id);

    // Maintenance Plan Management
    List<MaintenancePlan> getAllMaintenancePlans();
    MaintenancePlan getMaintenancePlanById(Long id);
    MaintenancePlan createMaintenancePlan(MaintenancePlanDTO dto);
    MaintenancePlan updateMaintenancePlan(Long id, MaintenancePlanDTO dto);
    void deleteMaintenancePlan(Long id);

    // Appointments Management
    List<ServiceAppointment> getAllAppointments();
    List<ServiceAppointment> getAppointmentsByStatus(String status);
    void deleteAppointment(Long id);

    // Payments Management
    List<Payment> getAllPayments();
    List<Payment> getPaymentsByStatus(PaymentStatus status);

    // Statistics & Reports
    DashboardStatsDTO getDashboardStats();
    Double getTotalRevenue(String startDate, String endDate);
    Map<Integer, Long> getAppointmentsByMonth(int year);
}