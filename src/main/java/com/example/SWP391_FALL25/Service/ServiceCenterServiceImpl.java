package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.ServiceCenterDTO;
import com.example.SWP391_FALL25.Entity.ServiceCenter;
import com.example.SWP391_FALL25.Repository.ServiceCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceCenterServiceImpl implements ServiceCenterService{
    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    @Override
    public List<ServiceCenterDTO> getAllServiceCenter() {
        List<ServiceCenter> centers = serviceCenterRepository.findAll();
        return centers.stream().map(center -> {
            ServiceCenterDTO dto = new ServiceCenterDTO();
            dto.setId(center.getId());
            dto.setName(center.getName());
            dto.setLocation(center.getLocation());
            dto.setContactNumber(center.getContactNumber());
            return dto;
        }).collect(Collectors.toList());
    }

    private ServiceCenterDTO convertDTO(ServiceCenter serviceCenter){
        ServiceCenterDTO serviceCenterDTO = new ServiceCenterDTO();
        serviceCenterDTO.setId(serviceCenter.getId());
        serviceCenterDTO.setName(serviceCenter.getName());
        serviceCenterDTO.setLocation(serviceCenter.getLocation());
        serviceCenterDTO.setContactNumber(serviceCenter.getContactNumber());
        return serviceCenterDTO;
    }

}
