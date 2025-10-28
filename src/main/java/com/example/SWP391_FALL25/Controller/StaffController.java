package com.example.SWP391_FALL25.Controller;


import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import com.example.SWP391_FALL25.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @Autowired
    private StaffService staffService;

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignTechnican(@PathVariable("id") Long appointmentId,@RequestParam Long technicianId){
        ServiceAppointment updatedAppointment = serviceAppointmentService.assignTechnician(appointmentId, technicianId);
        return ResponseEntity.ok(updatedAppointment);
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getAllAppointments(){
        List<ServiceAppointment> appointments = staffService.getAllAppointmentsSorted();
        return ResponseEntity.ok(appointments);
    }


}
