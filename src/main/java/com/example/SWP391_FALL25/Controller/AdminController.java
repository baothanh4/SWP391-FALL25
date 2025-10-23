package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.ServiceCenterDTO;
import com.example.SWP391_FALL25.DTO.Auth.DashboardStatsDTO;
import com.example.SWP391_FALL25.DTO.Auth.PartTypeDTO;
import com.example.SWP391_FALL25.DTO.Auth.MaintenancePlanDTO;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Enum.PaymentStatus;
import com.example.SWP391_FALL25.Repository.MaintenancePlanItemRepository;
import com.example.SWP391_FALL25.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MaintenancePlanItemRepository maintenancePlanItemRepository;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(adminService.updateUser(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<Users>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(adminService.getUsersByRole(role));
    }



    @GetMapping("/service-centers")
    public ResponseEntity<List<ServiceCenter>> getAllServiceCenters() {
        return ResponseEntity.ok(adminService.getAllServiceCenters());
    }

    @GetMapping("/service-centers/{id}")
    public ResponseEntity<ServiceCenter> getServiceCenterById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getServiceCenterById(id));
    }

    @PostMapping("/service-centers")
    public ResponseEntity<ServiceCenter> createServiceCenter(@RequestBody ServiceCenterDTO dto) {
        return ResponseEntity.ok(adminService.createServiceCenter(dto));
    }

    @PutMapping("/service-centers/{id}")
    public ResponseEntity<ServiceCenter> updateServiceCenter(@PathVariable Long id, @RequestBody ServiceCenterDTO dto) {
        return ResponseEntity.ok(adminService.updateServiceCenter(id, dto));
    }

    @DeleteMapping("/service-centers/{id}")
    public ResponseEntity<String> deleteServiceCenter(@PathVariable Long id) {
        adminService.deleteServiceCenter(id);
        return ResponseEntity.ok("Service center deleted successfully");
    }



    @GetMapping("/parts")
    public ResponseEntity<List<Part>> getAllParts() {
        return ResponseEntity.ok(adminService.getAllParts());
    }

    @GetMapping("/parts/{id}")
    public ResponseEntity<Part> getPartById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getPartById(id));
    }

    @PostMapping("/parts")
    public ResponseEntity<Part> createPart(@RequestBody PartDTO dto, @RequestParam Long partTypeId) {
        return ResponseEntity.ok(adminService.createPart(dto, partTypeId));
    }

    @PutMapping("/parts/{id}")
    public ResponseEntity<Part> updatePart(@PathVariable Long id, @RequestBody PartDTO dto) {
        return ResponseEntity.ok(adminService.updatePartAdmin(id, dto));
    }

    @DeleteMapping("/parts/{id}")
    public ResponseEntity<String> deletePart(@PathVariable Long id) {
        adminService.deletePart(id);
        return ResponseEntity.ok("Part deleted successfully");
    }



    @GetMapping("/part-types")
    public ResponseEntity<List<PartType>> getAllPartTypes() {
        return ResponseEntity.ok(adminService.getAllPartTypes());
    }

    @PostMapping("/part-types")
    public ResponseEntity<PartType> createPartType(@RequestBody PartTypeDTO dto) {
        return ResponseEntity.ok(adminService.createPartType(dto));
    }

    @PutMapping("/part-types/{id}")
    public ResponseEntity<PartType> updatePartType(@PathVariable Long id, @RequestBody PartTypeDTO dto) {
        return ResponseEntity.ok(adminService.updatePartType(id, dto));
    }

    @DeleteMapping("/part-types/{id}")
    public ResponseEntity<String> deletePartType(@PathVariable Long id) {
        adminService.deletePartType(id);
        return ResponseEntity.ok("Part type deleted successfully");
    }

    @GetMapping("/parts/low-stock")
    public ResponseEntity<List<Part>> getLowStockParts(@RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(adminService.getLowStockParts(threshold));
    }



    @GetMapping("/maintenance-plans")
    public ResponseEntity<List<MaintenancePlan>> getAllMaintenancePlans() {
        return ResponseEntity.ok(adminService.getAllMaintenancePlans());
    }

    @GetMapping("/maintenance-plans/{id}")
    public ResponseEntity<MaintenancePlan> getMaintenancePlanById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getMaintenancePlanById(id));
    }

    @PostMapping("/maintenance-plans")
    public ResponseEntity<MaintenancePlan> createMaintenancePlan(@RequestBody MaintenancePlanDTO dto) {
        return ResponseEntity.ok(adminService.createMaintenancePlan(dto));
    }

    @PutMapping("/maintenance-plans/{id}")
    public ResponseEntity<MaintenancePlan> updateMaintenancePlan(@PathVariable Long id, @RequestBody MaintenancePlanDTO dto) {
        return ResponseEntity.ok(adminService.updateMaintenancePlan(id, dto));
    }

    @DeleteMapping("/maintenance-plans/{id}")
    public ResponseEntity<String> deleteMaintenancePlan(@PathVariable Long id) {
        adminService.deleteMaintenancePlan(id);
        return ResponseEntity.ok("Maintenance plan deleted successfully");
    }



    @GetMapping("/appointments")
    public ResponseEntity<List<ServiceAppointment>> getAllAppointments() {
        return ResponseEntity.ok(adminService.getAllAppointments());
    }

    @GetMapping("/appointments/status/{status}")
    public ResponseEntity<List<ServiceAppointment>> getAppointmentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(adminService.getAppointmentsByStatus(status));
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        adminService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }



    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(adminService.getAllPayments());
    }

    @GetMapping("/payments/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(adminService.getPaymentsByStatus(status));
    }



    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/reports/revenue")
    public ResponseEntity<Double> getTotalRevenue(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(adminService.getTotalRevenue(startDate, endDate));
    }

    @GetMapping("/reports/appointments-by-month")
    public ResponseEntity<?> getAppointmentsByMonth(@RequestParam int year) {
        return ResponseEntity.ok(adminService.getAppointmentsByMonth(year));
    }

    @GetMapping("/maintenance-item")
    public ResponseEntity<?> getAllMaintenanceItems(){
        return ResponseEntity.ok(maintenancePlanItemRepository.findAll());
    }
}