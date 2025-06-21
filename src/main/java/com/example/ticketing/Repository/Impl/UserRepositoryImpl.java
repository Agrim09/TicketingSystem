package com.example.ticketing.Repository.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.ticketing.Config.Variable.ApplicationConstant;
import com.example.ticketing.Model.User;
import com.example.ticketing.Repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(@Qualifier(ApplicationConstant.BEAN_JDBC_TICKET) JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
    }
    
     private static class UserRowMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int row) throws SQLException {
			User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setuName(rs.getString("username"));
			return user;
		}
	}

}
