package com.example.ticketing.Repository.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.ticketing.Config.Variable.ApplicationConstant;
import com.example.ticketing.Model.Inventory;
import com.example.ticketing.Repository.InventoryRepository;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public InventoryRepositoryImpl(@Qualifier(ApplicationConstant.BEAN_JDBC_TICKET) JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Inventory getStock(int ticketId) {
        String sql = "SELECT * FROM inventory WHERE ticket_id = ?";
        List<Inventory> inventories = jdbcTemplate.query(sql, new InventoryRowMapper(), ticketId);
        return inventories.isEmpty() ? null : inventories.get(0);
    }
    

    @Override
    public Boolean reduceStock( int ticketId, int quantity ) {
        String sql = "UPDATE inventory SET available_stock = available_stock - ? WHERE ticket_id = ? AND available_stock >= ?";
        int rowsAffected = jdbcTemplate.update(sql, quantity, ticketId, quantity);
        return rowsAffected > 0;
    }

    private static class InventoryRowMapper implements RowMapper<Inventory> {
		@Override
		public Inventory mapRow(ResultSet rs, int row) throws SQLException {
			Inventory inventory = new Inventory();
			inventory.setId(rs.getInt("ID"));
			inventory.setEventId(rs.getString("EVENT_ID"));
            inventory.setPrice(rs.getInt("PRICE"));
            inventory.setAvailableStock(rs.getInt("AVAILABLE_STOCK"));
			return inventory;
		}
	}
    
}
