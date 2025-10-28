package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.Entity.ServiceAppointment;
import com.example.SWP391_FALL25.Enum.AppointmentStatus;
import com.example.SWP391_FALL25.Repository.ServiceAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private ServiceAppointmentRepository appointmentRepository;


    @Override
    public List<ServiceAppointment> getAllAppointmentsSorted() {
        return appointmentRepository.findAll();

    }
}
