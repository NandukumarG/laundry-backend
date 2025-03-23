package com.example.laundryproject.repository;

import com.example.laundryproject.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Auth findByUsername(String username);
    Auth findByEmail(String email);
}