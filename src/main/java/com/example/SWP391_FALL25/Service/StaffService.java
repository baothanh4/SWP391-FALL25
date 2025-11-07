    package com.example.SWP391_FALL25.Service;
    
    import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
    import com.example.SWP391_FALL25.Entity.*;
    import java.util.List;
    
    public interface StaffService {
    
        List<AppointmentDTO> getAllAppointments();

        void updateAppointmentStatus(Long appointmentId);
    }
