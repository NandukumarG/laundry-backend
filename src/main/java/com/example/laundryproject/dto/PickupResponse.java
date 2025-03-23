package com.example.laundryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

class PickupResponse {
    private String orderId;
    private String message;
    private Double totalPrice;
}