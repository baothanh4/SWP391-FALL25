package com.example.SWP391_FALL25.Controller;


import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignTechnican(@PathVariable("id") Long appointmentId,@RequestParam String technicanName){
        return ResponseEntity.ok(serviceAppointmentService.assignTechnican(appointmentId,technicanName));
    }
}
