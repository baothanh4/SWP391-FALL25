package com.example.SWP391_FALL25.Controller;


import com.example.SWP391_FALL25.DTO.Auth.*;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.Vehicle;
import com.example.SWP391_FALL25.Service.CustomerService;
import com.example.SWP391_FALL25.Service.ReminderService;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import com.example.SWP391_FALL25.Service.ServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @Autowired
    private ServiceCenterService serviceCenterService;

    @Autowired
    private final ReminderService reminderService;

    public CustomerController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping("/{id}/add-car")
    public Vehicle allCar(@PathVariable(name = "id") Long id,@RequestBody VehicleDTO vehicleDTO){
        return customerService.addCar(id,vehicleDTO);
    }

    @PostMapping("/appointment/create/{vehicleId}/{serviceCenterId}")
    public ResponseEntity<?> createAppointment(@PathVariable Long vehicleId, @PathVariable Long serviceCenterId,@RequestBody ServiceAppointmentDTO dto){
        ServiceAppointment appointment=serviceAppointmentService.createAppointment(vehicleId,serviceCenterId,dto);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/service-center")
    public ResponseEntity<?> getAllServiceCenter(){
        return ResponseEntity.ok(serviceCenterService.getAllServiceCenter());
    }

    @PatchMapping("/update-profile/{customerId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long customerId, @RequestBody RegisterRequest request){
        return ResponseEntity.ok(customerService.updateInformation(customerId, request));
    }

    @DeleteMapping("/delete/{vehicleId}")
    public void deleteCar(@PathVariable(name = "vehicleId")Long vehicleId){
        customerService.deleteCar(vehicleId);
    }

    @GetMapping("/vehicle/details/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable(name = "id")Long id){
        return ResponseEntity.ok(customerService.getVehicleById(id));
    }

    @GetMapping("/{userId}/appointments")
    public ResponseEntity<List<ServiceAppointment>> getUserAppointments(@PathVariable Long userId) {
        List<ServiceAppointment> appointments = customerService.getAppointmentByUser(userId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/totalcost/{appointmentId}")
    public ResponseEntity<DetailTotalCostResponseDTO> getQuotation(@PathVariable Long appointmentId) {
        DetailTotalCostResponseDTO detailTotalCostReport = customerService.getDetailTotalCostReport(appointmentId);
        return ResponseEntity.ok(detailTotalCostReport);
    }

    @PostMapping("/totalcost/{appointmentId}/approve")
    public ResponseEntity<?> approveQuotation(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, String> request) {
        String paymentMethod = request.getOrDefault("paymentMethod", "CASH");
        ServiceAppointment appointment = customerService.approveDetailTotalCostReport(
                appointmentId, paymentMethod);
        return ResponseEntity.ok(Map.of(
                "message", "Deatail total cost approved successfully",
                "appointment", appointment
        ));
    }

    @PostMapping("/totalcost/{appointmentId}/reject")
    public ResponseEntity<?> rejectQuotation(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, String> request) {
        String reason = request.getOrDefault("reason", "No reason provided");
        ServiceAppointment appointment = customerService.rejectDetailTotalCostReport(
                appointmentId, reason);
        return ResponseEntity.ok(Map.of(
                "message", "Deatail total cost rejected",
                "appointment", appointment
        ));
    }

    @PutMapping("/appointment/{Id}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable(name = "Id") Long appointmentId) {
        try {
            customerService.cancelAppointment(appointmentId);
            return ResponseEntity.ok("Appointment canceled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/vehicle/{vehicleId}/maintenance")
    public VehicleMaintenanceResponseDTO getVehicleMaintenance(@PathVariable Long vehicleId) {
        return reminderService.getVehicleMaintenance(vehicleId);
    }
}
