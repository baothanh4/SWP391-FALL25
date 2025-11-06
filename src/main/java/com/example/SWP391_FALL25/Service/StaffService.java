    package com.example.SWP391_FALL25.Service;
    
    import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
    import com.example.SWP391_FALL25.Entity.*;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import java.util.List;
    import java.util.Map;
    
    public interface StaffService {
    
        Page<AppointmentDTO> getAllAppointments(Pageable pageable, String search, String status, String priority, String sortBy);
        
        List<AppointmentDTO> getAllAppointments();
        
        Map<String, Object> getDashboardStatistics();
        
        AppointmentDTO getAppointmentById(Long appointmentId);
    }
