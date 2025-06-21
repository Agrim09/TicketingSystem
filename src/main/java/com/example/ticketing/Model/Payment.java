package com.example.ticketing.Model;

import java.sql.Date;

import com.example.ticketing.Constant.OrderStatus;

public class Payment {

    private String paymentId;
    private String orderId;
    private int totalPayment;
    private String paymentMethod;
    private Date paymentDate;
    private OrderStatus status; 

    public Payment () {
        
    }
    
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public int getTotalPayment() {
        return totalPayment;
    }
    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }
    
}
