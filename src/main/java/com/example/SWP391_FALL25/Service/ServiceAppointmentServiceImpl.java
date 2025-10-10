package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.ServiceAppointmentDTO;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceAppointmentServiceImpl implements ServiceAppointmentService{

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private MaintenancePlanRepository maintenancePlanRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public ServiceAppointment createAppointment(Long vehicleId, Long serviceId, ServiceAppointmentDTO dto){
        Vehicle vehicle=vehicleRepository.findById(vehicleId).orElseThrow(()->new RuntimeException("Vehicle not found"));
        ServiceCenter serviceCenter=serviceCenterRepository.findById(serviceId).orElseThrow(()->new RuntimeException("Service center not found"));

        ServiceAppointment serviceAppointment=new ServiceAppointment();
        serviceAppointment.setVehicle(vehicle);
        serviceAppointment.setServiceCenter(serviceCenter);
        serviceAppointment.setAppointmentDate(dto.getAppointmentDate());
        serviceAppointment.setAppointmentTime(dto.getAppointmentTime());
        serviceAppointment.setTechnicanAssigned("Pending");

        ServiceAppointment appointment=serviceAppointmentRepository.save(serviceAppointment);

        createReminder(vehicle, dto.getAppointmentDate());

        return appointment;
    }

    @Override
    public ServiceAppointment assignTechnican(Long appointmentId, String technicanName){
        ServiceAppointment appointment=serviceAppointmentRepository.findById(appointmentId).orElseThrow(()->new RuntimeException("Appointment not found"));
        appointment.setTechnicanAssigned(technicanName);

        if(appointment.getReport()==null){
            ServiceReport report=new ServiceReport();
            report.setReportDate(LocalDate.now());
            report.setAppointment(appointment);
            reportRepository.save(report);
        }
        return serviceAppointmentRepository.save(appointment);
    }


    private void createReminder(Vehicle vehicle, LocalDate appointmentDate){
        MaintenancePlan plan=maintenancePlanRepository.findByIntervalMonths(6).orElseThrow(()->new RuntimeException("Maintenance plan not found"));
        Reminder reminder=new Reminder();
        reminder.setVehicle(vehicle);
        reminder.setMaintenancePlan(plan);
        reminder.setStatus("Pending");
        reminder.setReminderDate(appointmentDate.plusMonths(plan.getIntervalMonths()));

        reminderRepository.save(reminder);
    }

    @Override
    public List<ServiceAppointment> getAppointmentsByTechnician(String technicanName) {
        return serviceAppointmentRepository.findByTechnicanAssigned(technicanName);
    }

}
