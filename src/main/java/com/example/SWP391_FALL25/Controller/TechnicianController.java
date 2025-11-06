package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Repository.ServiceAppointmentRepository;
import com.example.SWP391_FALL25.Repository.UserRepository;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import com.example.SWP391_FALL25.Service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/technician")
public class TechnicianController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ServiceAppointmentRepository appointmentRepository;

    // GET all technicians
    @GetMapping("/technicians")
    public ResponseEntity<?> getAllTechnicians() {
        try {
            List<Users> technicians = technicianService.getAllTechnicians();
            return ResponseEntity.ok(technicians);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch technicians: " + e.getMessage()));
        }
    }

    // GET technician by ID
    @GetMapping("/technicians/{technicianId}")
    public ResponseEntity<?> getTechnicianById(@PathVariable Long technicianId) {
        try {
            Users technician = technicianService.getTechnicianById(technicianId);
            return ResponseEntity.ok(technician);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch technician: " + e.getMessage()));
        }
    }

    @GetMapping("/{technicianId}/tasks")
    public ResponseEntity<?> getAppointments(
            @PathVariable(name = "technicianId") Long technicianId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy) {
        try {
            Users technician = userRepository.findById(technicianId)
                    .orElseThrow(() -> new IllegalArgumentException("TechnicianId not found"));
            
            Pageable pageable = PageRequest.of(page, size);
            Page<AppointmentDTO> appointments = technicianService.getTechnicianAppointments(
                    technician.getFullname(), pageable, search, status, sortBy);
            
            // Return in format expected by frontend
            Map<String, Object> response = Map.of(
                    "content", appointments.getContent(),
                    "totalItems", appointments.getTotalElements(),
                    "totalPages", appointments.getTotalPages()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch appointments: " + e.getMessage()));
        }
    }


    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long appointmentId) {
        try {
            System.out.println("=== GET APPOINTMENT BY ID ===");
            System.out.println("Appointment ID: " + appointmentId);
            
            AppointmentDTO appointment = technicianService.getAppointmentById(appointmentId);
            
            System.out.println("✅ Appointment loaded successfully");
            System.out.println("  - Status: " + appointment.getStatus());
            System.out.println("  - Customer: " + appointment.getCustomerName());
            System.out.println("  - Vehicle: " + appointment.getVehicleModel());
            System.out.println("  - Has Report: " + (appointment.getReport() != null));
            if (appointment.getReport() != null) {
                System.out.println("  - Report ID: " + appointment.getReport().getId());
                System.out.println("  - Current KM: " + appointment.getReport().getCurrentKm());
            }
            
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            System.err.println("❌ ERROR fetching appointment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch appointment: " + e.getMessage()));
        }
    }

    @PostMapping("/inspection/start/{appointmentId}")
    public ResponseEntity<?> startInspection(@PathVariable Long appointmentId) {
        try {
            System.out.println("=== START INSPECTION ===");
            System.out.println("Appointment ID: " + appointmentId);
            
            technicianService.startInspection(appointmentId);
            System.out.println("✅ Inspection started, status changed to INSPECTING");
            
            // Return the full appointment DTO with report info
            AppointmentDTO appointment = technicianService.getAppointmentById(appointmentId);
            
            if (appointment.getReport() != null) {
                System.out.println("✅ Report created with ID: " + appointment.getReport().getId());
            } else {
                System.err.println("⚠️ WARNING: Report is null after starting inspection!");
            }
            
            return ResponseEntity.ok(Map.of(
                    "message", "Inspection started",
                    "appointment", appointment
            ));
        } catch (Exception e) {
            System.err.println("❌ ERROR starting inspection: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to start inspection: " + e.getMessage()));
        }
    }

    @PostMapping("/totalcost/create/{reportId}")
    public ResponseEntity<?> createDetailTotalCostReport(
            @PathVariable Long reportId,
            @RequestBody List<ServiceReportDetailDTO> detailTotalCostItems) {
        List<ServiceReportDetails> details = technicianService.createDetailTotalCostReport(
                reportId, detailTotalCostItems);
        return ResponseEntity.ok(Map.of(
                "message", "Detail total cost report created and sent to customer",
                "items", details
        ));
    }

    @PostMapping("/repair/start/{appointmentId}")
    public ResponseEntity<?> startRepair(@PathVariable Long appointmentId) {
        ServiceAppointment appointment = technicianService.startRepair(appointmentId);
        return ResponseEntity.ok(Map.of(
                "message", "Repair work started",
                "appointment", appointment
        ));
    }

    @PostMapping("/{reportId}/details")
    public ResponseEntity<?> addReportDetails(@PathVariable(name = "reportId") Long reportId, @RequestBody List<ServiceReportDetailDTO> detailDTOS){
        List<ServiceReportDetails> details=serviceAppointmentService.addReportDetails(reportId,detailDTOS);
        return ResponseEntity.ok(details);
    }

    @PatchMapping("/reports/details/{detailId}")
    public ResponseEntity<ServiceReportDetails> updateDetails(@PathVariable(name="detailId") Long detailId,@RequestBody ServiceReportDetailDTO dto){
        ServiceReportDetails details=serviceAppointmentService.updateReportDetails(detailId,dto);
        return ResponseEntity.ok(details);
    }

    @PatchMapping("/part/{id}")
    public ResponseEntity<?> updatePart(@PathVariable(name = "id")Long id, @RequestBody PartDTO dto){
        return ResponseEntity.ok(technicianService.updatePart(id,dto));
    }

    @PutMapping("/{reportId}/update-by-km")
    public ResponseEntity<?> regenerateReportDetailsByKm(@PathVariable Long reportId,@RequestBody Map<String,Integer> request){
        try {
            Integer currentKm=request.get("currentKm");
            if(currentKm==null){
                return ResponseEntity.badRequest().body(Map.of("message", "Current kilometer is required"));
            }

            System.out.println("🔧 Analyzing kilometer: " + currentKm + " for report ID: " + reportId);
            
            List<ServiceReportDetails> updatedDetails=serviceAppointmentService.regenerateDetailsByKm(reportId,currentKm);
            
            System.out.println("📦 Returned " + updatedDetails.size() + " maintenance items");
            
            // If no recommendations found, return success with empty array and helpful message
            if (updatedDetails.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "data", new ArrayList<>(),
                    "message", "No maintenance plan found for " + currentKm + " km. Please add maintenance plans to the database or add services manually.",
                    "hint", "To add plans, insert records into MaintenancePlan and MaintenancePlanItem tables"
                ));
            }
            
            return ResponseEntity.ok(updatedDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage() != null ? e.getMessage() : "Failed to analyze kilometer",
                "error", e.getClass().getSimpleName()
            ));
        }
    }
    
    @PutMapping("/appointment/{appointmentId}/update-by-km")
    public ResponseEntity<?> regenerateReportDetailsByAppointmentKm(@PathVariable Long appointmentId,@RequestBody Map<String,Integer> request){
        try {
            Integer currentKm=request.get("currentKm");
            if(currentKm==null){
                return ResponseEntity.badRequest().body(Map.of("message", "Current kilometer is required"));
            }

            // Get the appointment and its report
            ServiceAppointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
            if (appointment.getReport() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "No report found for this appointment. Please start inspection first."));
            }

            List<ServiceReportDetails> updatedDetails=serviceAppointmentService.regenerateDetailsByKm(appointment.getReport().getId(),currentKm);
            
            // If no recommendations found, return success with empty array and helpful message
            if (updatedDetails.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "data", updatedDetails,
                    "message", "No maintenance recommendations found for " + currentKm + " km. You can add services manually."
                ));
            }
            
            return ResponseEntity.ok(updatedDetails);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage() != null ? e.getMessage() : "Failed to analyze kilometer",
                "error", e.getClass().getSimpleName()
            ));
        }
    }

    @GetMapping("/parts")
    public ResponseEntity<?> getAllParts() {
        try {
            System.out.println("=== GET ALL PARTS ===");
            List<PartDTO> parts = technicianService.getAllParts();
            System.out.println("✅ Found " + parts.size() + " parts");
            return ResponseEntity.ok(parts);
        } catch (Exception e) {
            System.err.println("❌ ERROR fetching parts: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch parts: " + e.getMessage()));
        }
    }

    @GetMapping("/debug/status")
    public ResponseEntity<?> getDebugStatus() {
        try {
            System.out.println("=== DEBUG STATUS CHECK ===");
            
            Map<String, Object> debug = new HashMap<>();
            
            // Check maintenance plans
            List<ServiceAppointment> allAppointments = appointmentRepository.findAll();
            debug.put("totalAppointments", allAppointments.size());
            
            List<Users> allTechnicians = userRepository.findByRole(com.example.SWP391_FALL25.Enum.Role.TECHNICIAN);
            debug.put("totalTechnicians", allTechnicians.size());
            
            List<com.example.SWP391_FALL25.Entity.Part> allParts = technicianService.getAllParts().stream()
                .map(dto -> {
                    com.example.SWP391_FALL25.Entity.Part p = new com.example.SWP391_FALL25.Entity.Part();
                    p.setId(dto.getId());
                    p.setName(dto.getName());
                    return p;
                })
                .collect(java.util.stream.Collectors.toList());
            debug.put("totalParts", allParts.size());
            
            debug.put("timestamp", java.time.LocalDateTime.now().toString());
            debug.put("status", "Backend is running");
            
            System.out.println("Debug info: " + debug);
            
            return ResponseEntity.ok(debug);
        } catch (Exception e) {
            System.err.println("❌ ERROR in debug status: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}