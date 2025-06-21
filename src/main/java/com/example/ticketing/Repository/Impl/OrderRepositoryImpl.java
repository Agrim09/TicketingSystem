package com.example.ticketing.Repository.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.ticketing.Config.Variable.ApplicationConstant;
import com.example.ticketing.Constant.OrderStatus;
import com.example.ticketing.Model.Order;
import com.example.ticketing.Repository.OrderRepository;

@Repository
public class OrderRepositoryImpl implements OrderRepository{

    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(@Qualifier(ApplicationConstant.BEAN_JDBC_TICKET) JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(Order order) {
        String sql = "INSERT INTO orders (order_id, ticket_id, user_id, event_id, quantity, request_date, status) "
            +" VALUES (?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql,
            order.getOrderId(),
            order.getTicketId(),
            order.getUserId(),
            order.getEventId(),
            order.getQuantity(),
            new java.sql.Timestamp(order.getRequestDate().getTime()),
            order.getStatus().toString().toUpperCase()  
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean updateStatus(String orderId, String status) {
        String sql = "UPDATE orders SET status = ?, lastupd_dtm = ?, lastupd_by= ? WHERE order_id = ?";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int rowsAffected = jdbcTemplate.update(sql, status, timestamp, "SYSTEM", orderId);
        return rowsAffected > 0;
    }

    @Override
    public Order getOrder(String orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        List<Order> orders = jdbcTemplate.query(sql, new OrderRowMapper(), orderId);
        return orders.isEmpty() ? null : orders.get(0);
    }
    
    private static class OrderRowMapper implements RowMapper<Order> {
		@Override
		public Order mapRow(ResultSet rs, int row) throws SQLException {
			Order order = new Order();
            order.setOrderId(rs.getString("order_id"));
            order.setTicketId(rs.getInt("ticket_id"));
            order.setUserId(rs.getInt("user_id"));
            order.setEventId(rs.getInt("event_id"));
            order.setQuantity(rs.getInt("quantity"));
            order.setRequestDate(rs.getDate("request_date"));
            order.setStatus(OrderStatus.valueOf(rs.getString("status")));
			return order;
		}
	}

}
