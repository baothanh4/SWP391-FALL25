package com.example.SWP391_FALL25.Service;

import com.example.SWP391_FALL25.DTO.Auth.PartDTO;
import com.example.SWP391_FALL25.Entity.Part;

public interface TechnicianService {
    Part updatePart(Long partId, PartDTO dto);
}
