package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.ServiceAppointmentDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;

public interface ServiceAppointmentService  {
    ServiceAppointment createAppointment(Long vehicleId, Long serviceId, ServiceAppointmentDTO dto);
}
