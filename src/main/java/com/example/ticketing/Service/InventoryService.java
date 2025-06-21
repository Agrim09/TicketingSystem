package com.example.ticketing.Service;

import com.example.ticketing.Model.Inventory;
import com.example.ticketing.Model.Order;

public interface InventoryService {

    boolean reduceStock ( Order orderRequest );
    
    boolean checkAvailibility ( Order orderRequest );

    Inventory getStock ( int ticketId );
    
}
