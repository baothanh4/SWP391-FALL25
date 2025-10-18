package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.VehicleDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Entity.Vehicle;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CustomerService {
    Vehicle addCar(Long customerID, VehicleDTO vehicleDTO);

    Users updateInformation(Long customerId, RegisterRequest request);

    void deleteCar( Long vehicleId);

    VehicleDTO getVehicleById(Long id);

    List<ServiceAppointment> getAppointmentByUser(Long userId);
}
