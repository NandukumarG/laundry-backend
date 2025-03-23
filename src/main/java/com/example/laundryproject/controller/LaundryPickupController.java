package com.example.laundryproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.laundryproject.dto.PickupRequest;
import com.example.laundryproject.model.LaundryPickup;
import com.example.laundryproject.service.LaundryPickupService;

@RestController
@RequestMapping("/api/pickups")
@CrossOrigin(origins = "http://localhost:3000")
public class LaundryPickupController {

    @Autowired
    private LaundryPickupService pickupService;

    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> schedulePickup(@RequestBody PickupRequest requestDTO) {
        try {
            LaundryPickup scheduledPickup = pickupService.schedulePickup(requestDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", scheduledPickup.getOrderId());
            response.put("message", "Pickup scheduled successfully");
            response.put("totalPrice", scheduledPickup.getTotalPrice());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to schedule pickup");
            errorResponse.put("message", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}