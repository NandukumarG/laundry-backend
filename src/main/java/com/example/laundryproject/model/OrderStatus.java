package com.example.laundryproject.model;

public enum OrderStatus {
    PLACED("Order Placed"),
    CONFIRMED("Order Confirmed"),
    PICKUP("Picked Up"),
    PROCESSING("Processing"),
    READY("Ready for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}