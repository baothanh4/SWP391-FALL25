package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import com.example.SWP391_FALL25.Service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technician")
public class TechnicianController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @Autowired
    private TechnicianService technicianService;

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

    @PatchMapping("/part/{id}")
    public ResponseEntity<?> updatePart(@PathVariable(name = "id")Long id, @RequestBody PartDTO dto){
        return ResponseEntity.ok(technicianService.updatePart(id,dto));
    }
}