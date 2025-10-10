package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.ServiceAppointmentDTO;
import com.example.SWP391_FALL25.DTO.Auth.ServiceReportDetailDTO;
import com.example.SWP391_FALL25.Entity.*;
import com.example.SWP391_FALL25.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private MaintenancePlanItemRepository maintenancePlanItemRepository;

    @Autowired
    private ServiceReportDetailsRepository serviceReportDetailsRepository;

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
    public List<ServiceReportDetails> addReportDetails(Long reportId, List<ServiceReportDetailDTO> reportDTO){
        ServiceReport report=reportRepository.findById(reportId).orElseThrow(()->new RuntimeException("Report not found"));

        List<ServiceReportDetails> savedDetails=new ArrayList<>();

        for(ServiceReportDetailDTO dto:reportDTO){
            Part part=dto.getPartId()!=null?partRepository.findById(dto.getPartId()).orElse(null):null;
            MaintenancePlanItem item=dto.getMaintenanceItemId()!=null?maintenancePlanItemRepository.findById(dto.getMaintenanceItemId()).orElse(null):null;

            ServiceReportDetails details=new ServiceReportDetails();
            details.setReport(report);
            details.setPart(part);
            details.setMaintenancePlanItem(item);
            details.setService(dto.getService());
            details.setActionType(dto.getActionType());
            details.setConditionStatus(dto.getConditionStatus());
            details.setLaborCost(dto.getLaborCost());
            details.setPartCost(dto.getPartCost());
            details.setTotalCost((dto.getLaborCost()!=0.0?dto.getLaborCost():0.0)+(dto.getPartCost()!=0.0?dto.getPartCost():0.0));

            savedDetails.add(serviceReportDetailsRepository.save(details));
        }
        return savedDetails;
    }

    @Override
    public List<ServiceAppointment> getAppointmentsByTechnician(String technicanName) {
        return serviceAppointmentRepository.findByTechnicanAssigned(technicanName);
    }



}
