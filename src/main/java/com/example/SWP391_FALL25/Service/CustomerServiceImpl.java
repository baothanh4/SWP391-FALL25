package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.VehicleDTO;
import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Entity.Vehicle;
import com.example.SWP391_FALL25.Repository.UserRepository;
import com.example.SWP391_FALL25.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Vehicle addCar(Long customerID, VehicleDTO vehicleDTO) {
        Users users=userRepository.findById(customerID).orElseThrow(()->new RuntimeException("Customer not found"));

        Vehicle vehicle=new Vehicle();
        vehicle.setVin(vehicleDTO.getVin());
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setOdometer(vehicleDTO.getOdometer());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setCustomer(users);

        return vehicleRepository.save(vehicle);
    }
}
