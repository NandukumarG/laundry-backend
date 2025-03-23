package com.example.laundryproject.dto;

import com.example.laundryproject.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }
}
