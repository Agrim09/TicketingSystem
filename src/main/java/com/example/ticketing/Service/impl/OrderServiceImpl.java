package com.example.ticketing.Service.impl;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing.Model.Order;
import com.example.ticketing.Repository.OrderRepository;
import com.example.ticketing.Service.InventoryService;
import com.example.ticketing.Service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public Order getOrder( String orderId ) {
        return orderRepository.getOrder( orderId );
    }
    
}
