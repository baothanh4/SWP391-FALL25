package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.Entity.Part;
import com.example.SWP391_FALL25.Repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TechnicianServiceImpl implements TechnicianService{

    @Autowired
    private PartRepository partRepository;


    @Override
    public Part updatePart(Long partId, PartDTO dto){
        Part part=partRepository.findById(partId).orElseThrow(()->new IllegalArgumentException("Part not found"));

        if(dto.getName()!=null && !dto.getName().isEmpty()){
            part.setName(dto.getName());
        }
        if(dto.getPrice()!=0.0){
            part.setPrice(dto.getPrice());
        }
        if(dto.getQuantity()!=0){
            part.setQuantity(part.getQuantity()- dto.getQuantity());
        }
        return partRepository.save(part);
    }


}
