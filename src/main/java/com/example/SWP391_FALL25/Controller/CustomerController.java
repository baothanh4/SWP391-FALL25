package com.example.SWP391_FALL25.Controller;


import com.example.SWP391_FALL25.DTO.Auth.RegisterRequest;
import com.example.SWP391_FALL25.DTO.Auth.ServiceAppointmentDTO;
import com.example.SWP391_FALL25.DTO.Auth.VehicleDTO;
import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Entity.Vehicle;
import com.example.SWP391_FALL25.Service.CustomerService;
import com.example.SWP391_FALL25.Service.ServiceAppointmentService;
import com.example.SWP391_FALL25.Service.ServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @Autowired
    private ServiceCenterService serviceCenterService;

    @PostMapping("/{id}/add-car")
    public Vehicle allCar(@PathVariable(name = "id") Long id,@RequestBody VehicleDTO vehicleDTO){
        return customerService.addCar(id,vehicleDTO);
    }

    @PostMapping("/appointment/create/{vehicleId}/{serviceCenterId}")
    public ResponseEntity<?> createAppointment(@PathVariable Long vehicleId, @PathVariable Long serviceCenterId,@RequestBody ServiceAppointmentDTO dto){
        ServiceAppointment appointment=serviceAppointmentService.createAppointment(vehicleId,serviceCenterId,dto);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/service-center")
    public ResponseEntity<?> getAllServiceCenter(){
        return ResponseEntity.ok(serviceCenterService.getAllServiceCenter());
    }

    @PatchMapping("/update-profile/{customerId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long customerId, @RequestBody RegisterRequest request){
        return ResponseEntity.ok(customerService.updateInformation(customerId, request));
    }

    @DeleteMapping("/delete/{vehicleId}")
    public void deleteCar(@PathVariable(name = "vehicleId")Long vehicleId){
        customerService.deleteCar(vehicleId);
    }

    @GetMapping("/vehicle/details/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable(name = "id")Long id){
        return ResponseEntity.ok(customerService.getVehicleById(id));
    }

    @GetMapping("/{userId}/appointments")
    public ResponseEntity<List<ServiceAppointment>> getUserAppointments(@PathVariable Long userId) {
        List<ServiceAppointment> appointments = customerService.getAppointmentByUser(userId);
        return ResponseEntity.ok(appointments);
    }

}
