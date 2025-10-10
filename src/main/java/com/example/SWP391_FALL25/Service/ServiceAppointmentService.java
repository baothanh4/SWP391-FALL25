package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.ServiceAppointmentDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import java.util.List;

public interface ServiceAppointmentService {
    ServiceAppointment createAppointment(Long vehicleId, Long serviceId, ServiceAppointmentDTO dto);

    ServiceAppointment assignTechnican(Long appointmentId, String technicanName);

    List<ServiceAppointment> getAppointmentsByTechnician(String technicanName);

}
