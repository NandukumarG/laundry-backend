package com.example.laundryproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PickupRequest {
    private String store;
    private String serviceType;
    private Map<String, Map<String, Integer>> clothingItems;
    private String pickupAddress;
    private String pickupDate; // Will be converted to LocalDate
    private String pickupTime;
    private String deliveryAddress;
    private String deliveryDate; // Will be converted to LocalDate
    private String deliveryTime;
    private Double totalPrice;
}