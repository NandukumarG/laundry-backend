package com.example.laundryproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.laundryproject.model.LaundryPickup;

@Repository
public interface LaundryPickupRepository extends JpaRepository<LaundryPickup, Long> {
    // Add custom query methods if needed
}