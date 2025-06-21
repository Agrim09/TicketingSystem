package com.example.ticketing.Kafka.Listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.ticketing.Constant.OrderStatus;
import com.example.ticketing.Kafka.KafkaService;
import com.example.ticketing.Model.Payment;
import com.example.ticketing.Repository.PaymentRepository;

@Component
public class PaymentListener {
    
    private static final Logger LOG = LogManager.getLogger(PaymentListener.class);

    private final PaymentRepository paymentRepository;
    private final KafkaService kafkaService;

    public PaymentListener(KafkaService kafkaService, PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.kafkaService = kafkaService;
    }

    @KafkaListener(topics = "payment-topic", groupId = "service-group")
    public void completePayment(String message) {
        LOG.info("Received message from payment-topic: {}", message);

        try {
          
            String pattern = "ORDER ID :(.*?), PAYMENT ID :(.*)";
            
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(message);
            
            String orderId = "";
            String paymentId = "";
            if (m.find()) {
                orderId = m.group(1); 
                paymentId = m.group(2); 
            }

            Payment payment = paymentRepository.getPayment(paymentId);
            if (payment != null && payment.getStatus() == OrderStatus.PENDING) {
                paymentRepository.updateStatus(payment.getPaymentId(), OrderStatus.COMPLETED.toString().toUpperCase());
                
                String notificationMessage = "Payment successfully completed for Order: " + orderId;
                kafkaService.sendMessage("notification-topic", notificationMessage);
                LOG.info("Notification sent to Kafka for order ID: {}", orderId);
            }

        } catch (Exception e) {
            LOG.error("Error completing payment for order ID: {}", message, e);
        }
    }

}
