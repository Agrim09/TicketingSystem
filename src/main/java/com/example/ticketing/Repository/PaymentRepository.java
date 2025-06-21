package com.example.ticketing.Repository;

import com.example.ticketing.Model.Payment;

public interface PaymentRepository {
    
    Payment getPayment( String paymentId );

    boolean save(Payment paymentRequest);

    boolean updateStatus ( String paymentId, String status );

}
