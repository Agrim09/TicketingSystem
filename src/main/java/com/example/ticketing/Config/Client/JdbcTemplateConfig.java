package com.example.ticketing.Config.Client;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.ticketing.Config.Variable.ApplicationConstant;

@Configuration
public class JdbcTemplateConfig {
    
    @Autowired
    @Qualifier(ApplicationConstant.BEAN_DATASOURCE_TICKET)
    private DataSource dataSource;

    @Bean(ApplicationConstant.BEAN_JDBC_TICKET)
    public JdbcTemplate initJdbc() {
        return new JdbcTemplate(dataSource);
    }

    @Autowired
    @Qualifier(ApplicationConstant.BEAN_TRANSACTION_MANAGER)
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(ApplicationConstant.BEAN_TRANSACTION_TEMPLATE)
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(platformTransactionManager());
    }

}
