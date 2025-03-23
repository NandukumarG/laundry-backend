package com.example.laundryproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.example.laundryproject.model.LaundryPickup;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LaundryPickupRepository extends JpaRepository<LaundryPickup, Long> {
    // Add custom query methods if needed
    // Find order by orderId
    Optional<LaundryPickup> findByOrderId(String orderId);

    // Find all orders sorted by pickup date (descending)
    ResponseEntity<LaundryPickup> findAllByOrderByPickupDateDesc();

    // Find orders by status
    ResponseEntity<LaundryPickup> findByStatusOrderByPickupDateDesc(String status);

    // Find orders by service type
    ResponseEntity<LaundryPickup> findByServiceTypeOrderByPickupDateDesc(String serviceType);

    // Find orders by pickup date after a certain date
    ResponseEntity<LaundryPickup> findByPickupDateAfterOrderByPickupDateDesc(LocalDate date);

    // Find orders by store
    ResponseEntity<LaundryPickup> findByStoreOrderByPickupDateDesc(String store);

    // Find orders by multiple parameters (status and service type)
    ResponseEntity<LaundryPickup> findByStatusAndServiceTypeOrderByPickupDateDesc(String status, String serviceType);

    // Find orders by pickup date range
    ResponseEntity<LaundryPickup> findByPickupDateBetweenOrderByPickupDateDesc(LocalDate startDate, LocalDate endDate);
}