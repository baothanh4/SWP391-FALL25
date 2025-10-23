package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationResponseDTO {
    private Long appointmentId;
    private Long reportId;
    private LocalDate inspectionDate;
    private String vehicleInfo;
    private List<QuotationItemDTO> items;
    private Double totalLaborCost;
    private Double totalPartCost;
    private Double grandTotal;
    private String status;
}
