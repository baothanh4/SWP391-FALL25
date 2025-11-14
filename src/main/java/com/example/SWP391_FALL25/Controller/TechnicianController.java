package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.DTO.Auth.UpdateUserProfileRequest;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Repository.UserRepository;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import com.example.SWP391_FALL25.Service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update-profile/{technicianId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long technicianId, @RequestBody UpdateUserProfileRequest request) {
        return ResponseEntity.ok(technicianService.updateTechnicianProfile(technicianId, request));
    }

    @GetMapping("/{technicianId}/tasks")
    public List<ServiceAppointment> getAppointments(@PathVariable(name = "technicianId")Long technicianId) {
        Users techician=userRepository.findById(technicianId).orElseThrow(()->new IllegalArgumentException("TechnicianId not found"));
        return serviceAppointmentService.getAppointmentsByTechnician(techician.getFullname());
    }


    @PostMapping("/inspection/start/{appointmentId}")
    public ResponseEntity<?> startInspection(@PathVariable Long appointmentId) {
        ServiceAppointment appointment = technicianService.startInspection(appointmentId);
        return ResponseEntity.ok(Map.of(
                "message", "Inspection started",
                "appointment", appointment
        ));
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

    @PostMapping("/{reportId}/details/by-km")
    public ResponseEntity<?> createReportDetailsByKm(
            @PathVariable(name = "reportId") Long reportId,
            @RequestBody Map<String, Integer> request) {

        Integer currentKm = request.get("currentKm");
        if (currentKm == null) {
            throw new RuntimeException("currentKm is required");
        }

        // Tạo details từ MaintenancePlan dựa trên km
        List<ServiceReportDetails> details = serviceAppointmentService.createDetailsByKm(reportId, currentKm);

        return ResponseEntity.ok(Map.of(
                "message", "Report details created from maintenance plan",
                "items", details
        ));
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
        Integer currentKm=request.get("currentKm");
        if(currentKm==null){
            throw new RuntimeException("CurrentKm is required");
        }

        List<ServiceReportDetails> updatedDetails=serviceAppointmentService.regenerateDetailsByKm(reportId,currentKm);
        return ResponseEntity.ok(updatedDetails);
    }

    @PostMapping("/{appointmentId}/send-report")
    public ResponseEntity<String> sendReportToCustomer(@PathVariable Long appointmentId) {
        serviceAppointmentService.sendReportToCustomer(appointmentId);
        return ResponseEntity.ok("Report has been sent to customer.");
    }

}