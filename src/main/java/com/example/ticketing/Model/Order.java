package com.example.ticketing.Model;

import java.util.Date;

import com.example.ticketing.Constant.OrderStatus;
import com.example.ticketing.Object.OrderRequest;

public class Order {

    private String orderId;
    private int ticketId;
    private int userId;
    private int eventId;
    private int quantity;
    private Date requestDate;
    private OrderStatus status; 

    public Order () {
        
    }

    public Order ( OrderRequest orderRequest ) {
        this.orderId = orderRequest.getOrderId();
        this.ticketId = orderRequest.getTicketId();
        this.eventId = orderRequest.getEventId();
        this.quantity = orderRequest.getQuantity();
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public int getTicketId() {
        return ticketId;
    }
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Date getRequestDate() {
        return requestDate;
    }
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
}
