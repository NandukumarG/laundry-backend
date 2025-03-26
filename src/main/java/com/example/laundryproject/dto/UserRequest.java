package com.example.laundryproject.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String fullName;
    private String username;
    private String phone;
    private String address;
    private String avatarUrl;
    private String email;
    private String password;

    public UserRequest(String fullName, String email, String phone, String address, String avatarUrl) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.avatarUrl = avatarUrl;
    }


}
