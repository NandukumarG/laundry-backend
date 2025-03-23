package com.example.laundryproject.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.example.laundryproject.model.LaundryPickup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.laundryproject.dto.PickupRequest;
import com.example.laundryproject.repository.LaundryPickupRepository;

@Service
public class LaundryPickupService {

    @Autowired
    private LaundryPickupRepository pickupRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LaundryPickup schedulePickup(PickupRequest requestDTO) {
        LaundryPickup pickup = new LaundryPickup();

        // Set basic properties
        pickup.setStore(requestDTO.getStore());
        pickup.setServiceType(requestDTO.getServiceType());
        pickup.setClothingItems(requestDTO.getClothingItems());
        pickup.setPickupAddress(requestDTO.getPickupAddress());
        pickup.setPickupDate(LocalDate.parse(requestDTO.getPickupDate(), formatter));
        pickup.setPickupTime(requestDTO.getPickupTime());
        pickup.setDeliveryAddress(requestDTO.getDeliveryAddress());
        pickup.setDeliveryDate(LocalDate.parse(requestDTO.getDeliveryDate(), formatter));
        pickup.setDeliveryTime(requestDTO.getDeliveryTime());
        pickup.setTotalPrice(requestDTO.getTotalPrice());

        // Generate a unique order ID
        String orderId = generateOrderId();
        pickup.setOrderId(orderId);

        // Save to database
        return pickupRepository.save(pickup);
    }

    private static String generateOrderId() {
        // Current year + month + random UUID part
        String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "LAUN-" + yearMonth + "-" + randomPart;
    }
}