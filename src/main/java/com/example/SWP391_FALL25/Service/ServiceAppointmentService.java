package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.ServiceAppointmentDTO;
import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ServiceAppointmentService {
    ServiceAppointment createAppointment(Long vehicleId, Long serviceId, ServiceAppointmentDTO dto);


    ServiceAppointment assignTechnician(Long appointmentId, Long technicianId);

    List<ServiceReportDetails> addReportDetails(Long reportId, List<ServiceReportDetailDTO> reportDTO);

    List<ServiceAppointment> getAppointmentsByTechnician(String fullName);

    @Transactional
    List<ServiceReportDetails> createDetailsByKm(Long reportId, Integer currentKm);

    ServiceReportDetails updateReportDetails(Long detailsId, ServiceReportDetailDTO dto);

    @Transactional
    List<ServiceReportDetails> regenerateDetailsByKm(Long reportId, Integer currentKm);
}
