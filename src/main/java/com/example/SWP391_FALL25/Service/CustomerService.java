package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.DetailTotalCostResponseDTO;
import com.example.SWP391_FALL25.DTO.Auth.FeedbackUpdateDTO;
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

    List<ServiceAppointment> getAppointmentByUser(Long userId);

    DetailTotalCostResponseDTO getDetailTotalCostReport(Long appointmentId);

    String approveReport(Long appointmentId, FeedbackUpdateDTO dto);

    String rejectReport(Long appointmentId, String feedback);

    @Transactional
    void cancelAppointment(Long appointmentId);

    ServiceAppointment approveDetailTotalCostReport(Long appointmentId, String paymentMethod);

    ServiceAppointment rejectDetailTotalCostReport(Long appointmentId, String reason);


}
