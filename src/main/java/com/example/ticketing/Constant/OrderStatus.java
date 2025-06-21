package com.example.ticketing.Constant;

public enum OrderStatus {
    
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
