package com.example.laundryproject.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.laundryproject.dto.ClothingItemsDTO;
import com.example.laundryproject.model.LaundryPickup;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public LaundryPickup updateDeliveryDetails(String orderId, String deliveryAddress, String deliveryDate, String deliveryTime) {

        // Find the existing pickup
        LaundryPickup pickup = getOrderById(orderId);
        if (pickup == null) {
            throw new RuntimeException("Order not found: " + orderId);
        }

        // Update only the delivery details
        if (deliveryAddress != null) {
            pickup.setDeliveryAddress(deliveryAddress);
        }
        if (deliveryDate != null) {
            pickup.setDeliveryDate(LocalDate.parse(deliveryDate));
        }
        if (deliveryTime != null) {
            pickup.setDeliveryTime(deliveryTime);
        }

        // Save and return the updated pickup
        return pickupRepository.save(pickup);
    }

    private static String generateOrderId() {
        // Current year + month + random UUID part
        String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "LAUN-" + yearMonth + "-" + randomPart;
    }

    public List<LaundryPickup> getAllPickups() {
        return pickupRepository.findAll();
    }

    public LaundryPickup getOrderById(String orderId) {
        return pickupRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    public List<LaundryPickup> getPickupsByServiceType(String serviceType) {
        return (List<LaundryPickup>) pickupRepository.findByServiceTypeOrderByPickupDateDesc(serviceType);
    }

    public List<LaundryPickup> getPickupsAfterDate(LocalDate date) {
        return (List<LaundryPickup>) pickupRepository.findByPickupDateAfterOrderByPickupDateDesc(date);
    }

    public List<LaundryPickup> getPickupsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return (List<LaundryPickup>) pickupRepository.findByPickupDateBetweenOrderByPickupDateDesc(startDate, endDate);
    }


    public boolean cancelPickup(String orderId) {
        Optional<LaundryPickup> pickup = pickupRepository.findByOrderId(orderId);
        if (pickup.isPresent()) {
            pickupRepository.delete(pickup.get());
            return true;
        } else {
            return false;
        }
    }
}