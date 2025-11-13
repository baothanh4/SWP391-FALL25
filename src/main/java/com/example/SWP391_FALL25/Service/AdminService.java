package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.ServiceCenterDTO;
import com.example.SWP391_FALL25.DTO.Auth.DashboardStatsDTO;
import com.example.SWP391_FALL25.DTO.Auth.PartTypeDTO;
import com.example.SWP391_FALL25.DTO.Auth.MaintenancePlanDTO;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Enum.PaymentStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminService {

    // Users

    Page<Users> getAllUsers(int page, int size);
    Users getUserById(Long id);
    Users updateUser(Long id, RegisterRequest request);
    void deleteUser(Long id);
    List<Users> getUsersByRole(String role);
    void unlockUser(Long id);



    // Service Centers
    List<ServiceCenter> getAllServiceCenters();
    ServiceCenter getServiceCenterById(Long id);
    ServiceCenter createServiceCenter(ServiceCenterDTO dto);
    ServiceCenter updateServiceCenter(Long id, ServiceCenterDTO dto);
    void deleteServiceCenter(Long id);

    // Parts
    Page<Part> getAllParts(int page, int size);
    Part getPartById(Long id);
    Part createPart(PartDTO dto, Long partTypeId);
    Part updatePartAdmin(Long id, PartDTO dto);
    void deletePart(Long id);
    List<Part> getLowStockParts(int threshold);



    // Part Types
    List<PartType> getAllPartTypes();
    PartType createPartType(PartTypeDTO dto);
    PartType updatePartType(Long id, PartTypeDTO dto);
    void deletePartType(Long id);

    // Logs
    List<SystemLog> getAllSystemLog();
    Optional<SystemLog> getSystemLogById(Long id);

    // Maintenance
    List<MaintenancePlan> getAllMaintenancePlans();
    MaintenancePlan getMaintenancePlanById(Long id);
    MaintenancePlan createMaintenancePlan(MaintenancePlanDTO dto);

    Double getTotalCost();

    MaintenancePlan updateMaintenancePlan(Long id, MaintenancePlanDTO dto);
    void deleteMaintenancePlan(Long id);

    // Appointments
    // ✅ Pagination for Appointments
    Page<ServiceAppointment> getAllAppointments(int page, int size);
    List<ServiceAppointment> getAppointmentsByStatus(String status);
    void deleteAppointment(Long id);



    // Payments
    // ✅ Pagination for Payments
    Page<Payment> getAllPayments(int page, int size);
    List<Payment> getPaymentsByStatus(PaymentStatus status);



    // Dashboard
    DashboardStatsDTO getDashboardStats();
    Double getTotalRevenue(String startDate, String endDate);
    Map<Integer, Long> getAppointmentsByMonth(int year);
}