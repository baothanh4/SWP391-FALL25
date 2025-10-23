package com.example.SWP391_FALL25.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationItemDTO {
    private Long detailId;
    private String service;
    private String actionType;
    private String conditionStatus;
    private String partName;
    private Double partPrice;
    private Double laborCost;
    private Double subtotal;
}
