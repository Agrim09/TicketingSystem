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
import com.example.ticketing.Model.Payment;
import com.example.ticketing.Repository.PaymentRepository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepositoryImpl(@Qualifier(ApplicationConstant.BEAN_JDBC_TICKET) JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(Payment paymentRequest) {
        String sql = "INSERT INTO payments (payment_id, order_id, total_payment, payment_method, payment_date, status) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql,
            paymentRequest.getPaymentId(),
            paymentRequest.getOrderId(),
            paymentRequest.getTotalPayment(),
            paymentRequest.getPaymentMethod(),
            new Date(),
            paymentRequest.getStatus().toString().toUpperCase()
        );
        return rowsAffected > 0;
    }

    @Override
    public Payment getPayment(String paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        List<Payment> orders = jdbcTemplate.query(sql, new PaymentRowMapper(), paymentId);
        return orders.isEmpty() ? null : orders.get(0);
    }


       private static class PaymentRowMapper implements RowMapper<Payment> {
		@Override
		public Payment mapRow(ResultSet rs, int row) throws SQLException {
			Payment payment = new Payment();
            payment.setPaymentId(rs.getString("payment_id"));
            payment.setOrderId(rs.getString("order_id"));
            payment.setPaymentMethod(rs.getString("payment_method"));
            payment.setPaymentDate(rs.getDate("payment_date"));
            payment.setStatus(OrderStatus.valueOf(rs.getString("status")));
			return payment;
		}
	}


    @Override
    public boolean updateStatus(String paymentId, String status) {
        String sql = "UPDATE payments SET status = ? , lastupd_dtm = ?, lastupd_by = ? WHERE payment_id = ?";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int rowsAffected = jdbcTemplate.update(sql, status, timestamp, "SYSTEM", paymentId);
        return rowsAffected > 0;
    }
    
}
