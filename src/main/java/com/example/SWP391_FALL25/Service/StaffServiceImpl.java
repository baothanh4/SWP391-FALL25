package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Repository.ServiceAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private ServiceAppointmentRepository appointmentRepository;


    @Override
    public List<AppointmentDTO> getAllAppointments() {
        List<ServiceAppointment> appointments = appointmentRepository.findAll();
        List<AppointmentDTO> result = new ArrayList<>();

        for (ServiceAppointment a : appointments) {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setAppointmentId(a.getId());
            dto.setCustomerId(a.getVehicle().getCustomer().getId());  // lấy customer qua vehicle
            dto.setVehicleId(a.getVehicle().getId());
            dto.setServiceCenterId(a.getServiceCenter().getId());
            dto.setStatus(a.getStatus().name());
            dto.setAppointmentDate(a.getAppointmentDate());
            dto.setAppointmentTime(a.getAppointmentTime());
            dto.setTechnicianAssigned(a.getTechnicianAssigned());

            result.add(dto);
        }
        return result;
    }
}
