package com.example.ticketing.Kafka.Producer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.example.ticketing.Constant.OrderStatus;
import com.example.ticketing.Kafka.KafkaService;
import com.example.ticketing.Model.Order;
import com.example.ticketing.Repository.OrderRepository;
import com.example.ticketing.Service.InventoryService;
import com.example.ticketing.Util.CommonUtil;

@Component
public class ProducerOrder {

    private static final Logger LOG = LogManager.getLogger(ProducerOrder.class);

    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;
    private final KafkaService kafkaService;

    public ProducerOrder(InventoryService inventoryService, OrderRepository orderRepository, KafkaService kafkaService) {
        this.inventoryService = inventoryService;
        this.orderRepository = orderRepository;
        this.kafkaService = kafkaService;
    }

    public String confirmOrder(Order orderRequest, String paymentMethod) {
        LOG.info("Confirming order request for ticket ID: {}", orderRequest.getTicketId());
    
        String orderId = null;
        boolean reduceStock = inventoryService.reduceStock(orderRequest);
        if (reduceStock) {
            LOG.info("Stock successfully reduced for ticket ID: {}", orderRequest.getTicketId());
    
            Order order = new Order();
            orderId = CommonUtil.generateTrxId("ORDER");
            order.setOrderId(orderId);
            order.setTicketId(orderRequest.getTicketId());
            order.setUserId(orderRequest.getUserId());
            order.setEventId(orderRequest.getEventId());
            order.setQuantity(orderRequest.getQuantity());
            order.setStatus(OrderStatus.PENDING);
            order.setRequestDate(orderRequest.getRequestDate());
    
            LOG.info("Saving order with ID: {}", orderId);
            if (orderRepository.save(order)) {
                LOG.info("Order saved successfully, sending to Kafka...");
                String message = " ORDER ID :" + orderId + ", PAYMENT METHOD :" + paymentMethod;
                kafkaService.sendMessage("order-topic", message);
                LOG.info("Message sent to Kafka: {}", message);
            } else {
                LOG.error("Failed to save order with ID: {}", orderId);
            }
        } else {
            LOG.warn("Stock not available for ticket ID: {}", orderRequest.getTicketId());
        }
    
        return orderId;
    }

}
