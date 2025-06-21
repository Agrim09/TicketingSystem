package com.example.ticketing.Controller;

import org.springframework.http.MediaType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.ticketing.Constant.StatusCode;
import com.example.ticketing.Kafka.Producer.ProducerOrder;
import com.example.ticketing.Model.Order;
import com.example.ticketing.Model.User;
import com.example.ticketing.Object.APIRequest;
import com.example.ticketing.Object.APIResponse;
import com.example.ticketing.Object.HeaderResponse;
import com.example.ticketing.Object.OrderRequest;
import com.example.ticketing.Object.OrderResponse;
import com.example.ticketing.Service.InventoryService;
import com.example.ticketing.Service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger LOG = LogManager.getLogger(OrderController.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProducerOrder producerOrder;

    private <T> APIRequest<T> requestMapper(String input, Class<T> clazz) {
        Gson gson = new Gson();
        Type typeObject = TypeToken.getParameterized(APIRequest.class, clazz).getType();
        return gson.fromJson(input, typeObject);
    }
    
    @RequestMapping(value = "/ticket", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<OrderResponse> orderTicket(@RequestBody String input) {
        LOG.traceEntry("Received request: {}", input);
        APIResponse<OrderResponse> response = new APIResponse<>();
        
        try {
            // Step 1: Mapping input JSON ke APIRequest
            LOG.info("Mapping input JSON to APIRequest object...");
            APIRequest<OrderRequest> request = requestMapper(input, OrderRequest.class);
            LOG.debug("Request mapped successfully: {}", request);
    
            // Step 2: Validasi user berdasarkan uName
            LOG.info("Fetching user details for username: {}", request.getHeader().getuName());
            User user = userService.getUser(request.getHeader().getuName());
            if (user == null) {
                LOG.warn("User not found: {}", request.getHeader().getuName());
                response.setHeader(new HeaderResponse(StatusCode.FAILED.getCode(), "INVALID USER"));
                return response;
            }
            LOG.info("User found: {} with ID: {}", user.getuName(), user.getUserId());
    
            // Step 3: Membuat order dari payload request
            LOG.info("Creating order from payload...");
            Order order = new Order(request.getPayload());
            
            // Parsing tanggal request
            LOG.info("Parsing request date: {}", request.getHeader().getRequestDS());
            Date requestDate = sdf.parse(request.getHeader().getRequestDS());
            order.setUserId(user.getUserId());
            order.setRequestDate(requestDate);
            LOG.debug("Order created successfully: {}", order);
    
            // Step 4: Cek ketersediaan stok tiket
            LOG.info("Checking inventory availability for event: {}", order.getEventId());
            boolean available = inventoryService.checkAvailibility(order);
            if (!available) {
                LOG.warn("Stock is not available for event: {}", order.getEventId());
                response.setHeader(new HeaderResponse(StatusCode.FAILED.getCode(), "STOCK IS NOT AVAILABLE"));
                return response;
            }
            LOG.info("Stock is available for event: {}", order.getEventId());
    
            // Step 5: Konfirmasi order ke Kafka
            LOG.info("Confirming order and generating order ID...");
            String orderId = producerOrder.confirmOrder(order, request.getPayload().getPaymentMethod());
            LOG.info("Order confirmed successfully with ID: {}", orderId);
            
            // Step 6: Menyusun response
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(orderId);
            response.setPayload(orderResponse);
            response.setHeader(new HeaderResponse(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.toString()));
    
            LOG.info("Order processing completed successfully.");
    
        } catch (ParseException e) {
            LOG.error("Date parsing error: {}", e.getMessage(), e);
            response.setHeader(new HeaderResponse(StatusCode.FAILED.getCode(), "INVALID DATE FORMAT"));
        } catch (Exception e) {
            LOG.error("Unexpected error: {}", e.getMessage(), e);
            response.setHeader(new HeaderResponse(StatusCode.GENERIC_ERROR.getCode(), StatusCode.GENERIC_ERROR.toString()));
        }
    
        LOG.info("Final response: {}", response);
        LOG.traceExit();
        return response;
    }
    

}
