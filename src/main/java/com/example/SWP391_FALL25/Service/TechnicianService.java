package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.Entity.Part;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;

import java.util.List;

public interface TechnicianService {
    Part updatePart(Long partId, PartDTO dto);

    ServiceAppointment startInspection(Long appointmentId);

    List<ServiceReportDetails> createQuotation(Long reportId, List<ServiceReportDetailDTO> items);

    ServiceAppointment startRepair(Long appointmentId);

}
