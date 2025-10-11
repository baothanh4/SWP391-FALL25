package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technician")
public class TechnicianController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @GetMapping("/appointments")
    public List<ServiceAppointment> getAppointments(@RequestParam String name) {
        return serviceAppointmentService.getAppointmentsByTechnician(name);
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
}