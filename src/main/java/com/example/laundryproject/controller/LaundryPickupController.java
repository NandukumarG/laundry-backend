package com.example.laundryproject.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping("/history")
    public ResponseEntity<List<LaundryPickup>> getAllPickups() {
        List<LaundryPickup> pickups = pickupService.getAllPickups();
        return ResponseEntity.ok(pickups);
    }

    @GetMapping("/service/{serviceType}")
    public ResponseEntity<List<LaundryPickup>> getPickupsByServiceType(@PathVariable String serviceType) {
        List<LaundryPickup> pickups = pickupService.getPickupsByServiceType(serviceType);
        return ResponseEntity.ok(pickups);
    }

    @GetMapping("/date/after")
    public ResponseEntity<List<LaundryPickup>> getPickupsAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LaundryPickup> pickups = pickupService.getPickupsAfterDate(date);
        return ResponseEntity.ok(pickups);
    }

    @GetMapping("/date/between")
    public ResponseEntity<List<LaundryPickup>> getPickupsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<LaundryPickup> pickups = pickupService.getPickupsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(pickups);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<LaundryPickup> getOrderById(@PathVariable String orderId) {
        LaundryPickup order = pickupService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelPickup(@PathVariable String orderId) {
        boolean isCancelled = pickupService.cancelPickup(orderId);

        if (isCancelled) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Pickup cancelled successfully");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to cancel pickup");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    @PatchMapping("/{orderId}")
    public ResponseEntity<LaundryPickup> updateDeliveryDetails(
            @PathVariable String orderId,
            @RequestBody Map<String, Object> deliveryDetails) {

        // Extract the delivery details
        String deliveryAddress = (String) deliveryDetails.get("deliveryAddress");
        String deliveryDate = (String) deliveryDetails.get("deliveryDate");
        String deliveryTime = (String) deliveryDetails.get("deliveryTime");

        // Update only the delivery details
        LaundryPickup updatedPickup = pickupService.updateDeliveryDetails(
                orderId, deliveryAddress, deliveryDate, deliveryTime);

        return ResponseEntity.ok(updatedPickup);
    }
}