package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.AppointmentDTO;
import com.example.SWP391_FALL25.DTO.Auth.DetailTotalCostResponseDTO;
import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.VehicleDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Entity.Vehicle;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CustomerService {
    Vehicle addCar(Long customerID, VehicleDTO vehicleDTO);

    Users updateInformation(Long customerId, RegisterRequest request);

    void deleteCar( Long vehicleId);

    VehicleDTO getVehicleById(Long id);

    List<AppointmentDTO> getAppointmentByUser(Long userId);

    DetailTotalCostResponseDTO getDetailTotalCostReport(Long appointmentId);

    @Transactional
    void cancelAppointment(Long appointmentId);

    ServiceAppointment approveDetailTotalCostReport(Long appointmentId, String paymentMethod);

    ServiceAppointment rejectDetailTotalCostReport(Long appointmentId, String reason);


}
