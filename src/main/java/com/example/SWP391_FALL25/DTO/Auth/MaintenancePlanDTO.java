package com.example.SWP391_FALL25.DTO.Auth;

import lombok.Data;
import java.util.List;

@Data
public class MaintenancePlanDTO {
    private Integer intervalKm;
    private Integer intervalMonths;
    private List<MaintenancePlanItemDTO> items;
}
