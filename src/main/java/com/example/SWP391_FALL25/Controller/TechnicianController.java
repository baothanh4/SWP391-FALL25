package com.example.SWP391_FALL25.Controller;

import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
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
}