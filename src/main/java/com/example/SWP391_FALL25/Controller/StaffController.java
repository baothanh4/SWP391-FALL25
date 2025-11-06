package com.example.SWP391_FALL25.Controller;


import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import com.example.SWP391_FALL25.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @Autowired
    private StaffService staffService;

    // GET dashboard statistics
    @GetMapping("/dashboard/statistics")
    public ResponseEntity<?> getDashboardStatistics() {
        try {
            Map<String, Object> stats = staffService.getDashboardStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch dashboard statistics: " + e.getMessage()));
        }
    }

    // GET appointment by ID
    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long appointmentId) {
        try {
            AppointmentDTO appointment = staffService.getAppointmentById(appointmentId);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch appointment: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignTechnican(@PathVariable("id") Long appointmentId,@RequestParam Long technicianId){
        ServiceAppointment updatedAppointment = serviceAppointmentService.assignTechnician(appointmentId, technicianId);
        return ResponseEntity.ok(updatedAppointment);
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String sortBy){
        
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appointments = staffService.getAllAppointments(pageable, search, status, priority, sortBy);
        return ResponseEntity.ok(appointments);
    }


}
