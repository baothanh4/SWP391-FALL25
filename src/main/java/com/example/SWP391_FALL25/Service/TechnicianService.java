package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.Entity.Part;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import com.example.SWP391_FALL25.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TechnicianService {
    Part updatePart(Long partId, PartDTO dto);

    ServiceAppointment startInspection(Long appointmentId);

    List<ServiceReportDetails> createDetailTotalCostReport(Long reportId, List<ServiceReportDetailDTO> items);

    ServiceAppointment startRepair(Long appointmentId);

    List<Users> getAllTechnicians();

    Users getTechnicianById(Long technicianId);
    
    Page<AppointmentDTO> getTechnicianAppointments(String technicianName, Pageable pageable, String search, String status, String sortBy);
    
    AppointmentDTO getAppointmentById(Long appointmentId);
    
    List<PartDTO> getAllParts();

}
