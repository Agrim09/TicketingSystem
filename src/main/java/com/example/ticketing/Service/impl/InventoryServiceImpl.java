package com.example.ticketing.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing.Constant.OrderStatus;
import com.example.ticketing.Model.Inventory;
import com.example.ticketing.Model.Order;
import com.example.ticketing.Repository.InventoryRepository;
import com.example.ticketing.Service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Override
    public boolean checkAvailibility( Order orderRequest ) {
        boolean available = true;
        Inventory inventory = inventoryRepository.getStock( orderRequest.getTicketId() );
        if ( inventory == null ) {
            return false;
        }
        int quantityLeft = inventory.getAvailableStock();
        if ( quantityLeft < orderRequest.getQuantity() ) {
            return false;
        }
        return available;
    }

    @Override
    public boolean reduceStock(Order orderRequest) {
        boolean reduced = inventoryRepository.reduceStock( orderRequest.getTicketId(), orderRequest.getQuantity() );
        return reduced;
    }

    @Override
    public Inventory getStock(int ticketId) {
        Inventory inventory = inventoryRepository.getStock( ticketId );
        return inventory;
    }
    
}
