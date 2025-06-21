package com.example.ticketing.Repository;

import com.example.ticketing.Model.Inventory;

public interface InventoryRepository {
    
    Inventory getStock ( int tikcetId );

    Boolean reduceStock ( int ticketId, int quantity );

}
