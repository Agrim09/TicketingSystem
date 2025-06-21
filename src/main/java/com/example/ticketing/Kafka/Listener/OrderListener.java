package com.example.ticketing.Kafka.Listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.ticketing.Constant.OrderStatus;
import com.example.ticketing.Kafka.KafkaService;
import com.example.ticketing.Model.Inventory;
import com.example.ticketing.Model.Order;
import com.example.ticketing.Model.Payment;
import com.example.ticketing.Repository.OrderRepository;
import com.example.ticketing.Repository.PaymentRepository;
import com.example.ticketing.Service.InventoryService;
import com.example.ticketing.Util.CommonUtil;

@Component
public class OrderListener {

    private static final Logger LOG = LogManager.getLogger(OrderListener.class);

    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;
    private final KafkaService kafkaService;
    private final PaymentRepository paymentRepository;

    public OrderListener(InventoryService inventoryService, OrderRepository orderRepository, PaymentRepository paymentRepository, KafkaService kafkaService) {
        this.inventoryService = inventoryService;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.kafkaService = kafkaService;
    }
    
    @KafkaListener(topics = "order-topic", groupId = "service-group")
    public void orderListener(String message) {
        LOG.info("Received message from order-topic: {}", message);

        try {
        
            String pattern = "ORDER ID :(.*?), PAYMENT METHOD :(.*)";
            
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(message);
            
            String orderId = "";
            String paymentMethod = "";
            if (m.find()) {
                orderId = m.group(1); 
                paymentMethod = m.group(2);
            }
            
            LOG.info("Processing payment for order ID: {}, Payment Method: {} ", orderId, paymentMethod);

            Order order = orderRepository.getOrder( orderId );
            if (order == null) {
                LOG.warn("Order not found for ID: {}", orderId );
                return;
            }

            Inventory inventory = inventoryService.getStock(order.getTicketId());
            if (inventory == null) {
                LOG.warn("Stock not found for ticket ID: {}", order.getTicketId());
                return;
            }

            Payment payment = new Payment();
            String paymentId = CommonUtil.generateTrxId("ORDER");
            payment.setPaymentId( paymentId );
            payment.setOrderId( orderId );
            int amount = order.getQuantity() * inventory.getPrice();
            payment.setTotalPayment(amount);
            payment.setPaymentMethod( paymentMethod );
            payment.setStatus( OrderStatus.PENDING );

            boolean paymentSuccess = paymentRepository.save(payment);
            if (paymentSuccess) {
                LOG.info("Payment saved successfully for order ID: {}", orderId);

                orderRepository.updateStatus(orderId, "CONFIRMED");
                LOG.info("Order status updated to CONFIRMED for order ID: {}", orderId);

                String notificationMessage = "Order completed processed for order: " + orderId;
                kafkaService.sendMessage("notification-topic", notificationMessage);
                LOG.info("Notification sent to Kafka for order ID: {}", orderId);

                String paymentMessage = " ORDER ID :" + orderId + ", PAYMENT ID :" + paymentId;
                kafkaService.sendMessage("payment-topic", paymentMessage);
                LOG.info("Payment initialization message sent to Kafka for order ID: {}", orderId);

            } else {
                LOG.error("Failed to save payment for order ID: {}", orderId);
            }

        } catch (Exception e) {
            LOG.error("Error processing payment message: {}", message, e);
        }
    }

}
