package com.example.ticketing.Config.Profile;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.ticketing.Config.Properties.AppProperties;
import com.example.ticketing.Config.Variable.ApplicationConstant;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile("local")
public class LocalProfile {
    
    @Bean(ApplicationConstant.BEAN_APP_CONFIG)
    public AppProperties loadConfig() {
        AppProperties appProperties = new AppProperties();
        appProperties.setAPP_NAME(ApplicationConstant.APP_NAME);
        appProperties.setHTTP_CLIENT_CONNECTION_TIMEOUT(ApplicationConstant.HTTP_CLIENT_CONNECTION_TIMEOUT);
        appProperties.setHTTP_CLIENT_READ_TIMEOUT(ApplicationConstant.HTTP_CLIENT_READ_TIMEOUT);
        appProperties.setHTTP_SOA_TIMEOUT(ApplicationConstant.SOA_TIMEOUT);
        appProperties.setUSER_IDLE_TIMEOUT(ApplicationConstant.USER_IDLE_TIMEOUT);

        return appProperties;
    }

    @Bean(ApplicationConstant.BEAN_DATASOURCE_TICKET)
    @ConfigurationProperties("datasource.ticketing")
    public DataSource loadDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    webServerFactoryCustomizer() {
        return factory -> factory.setContextPath(ApplicationConstant.APP_NAME);
    }

}
