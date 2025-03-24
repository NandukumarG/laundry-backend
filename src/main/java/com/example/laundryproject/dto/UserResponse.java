package com.example.laundryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserResponse {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatarUrl;
}