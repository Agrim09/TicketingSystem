package com.example.ticketing.Repository;

import com.example.ticketing.Model.Order;

public interface OrderRepository {

    boolean save ( Order order );
    
    boolean updateStatus ( String orderId, String status );

    Order getOrder ( String orderId );
    
}
