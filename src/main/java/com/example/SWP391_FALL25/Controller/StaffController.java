package com.example.SWP391_FALL25.Controller;


import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
import com.example.SWP391_FALL25.DTO.Auth.UpdateUserProfileRequest;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.Users;
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

    @PutMapping("/update-profile/{staffId}")
    public ResponseEntity<Users> updateStaffProfile(
            @PathVariable Long staffId,
            @RequestBody UpdateUserProfileRequest request) {

        Users updated = staffService.updateStaffProfile(staffId, request);
        return ResponseEntity.ok(updated);
    }


    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignTechnican(@PathVariable("id") Long appointmentId,@RequestParam Long technicianId){
        ServiceAppointment updatedAppointment = serviceAppointmentService.assignTechnician(appointmentId, technicianId);
        return ResponseEntity.ok(updatedAppointment);
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getAllAppointments(){
        List<AppointmentDTO> appointments = staffService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/appointments/{id}/send-car-back")
    public ResponseEntity<?> sendCarBack(@PathVariable(name = "id")  Long appointmentId){
        staffService.updateAppointmentStatus(appointmentId);
        return ResponseEntity.ok().body("Send car back completed");
    }

}
