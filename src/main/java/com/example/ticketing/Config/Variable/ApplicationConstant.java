package com.example.ticketing.Config.Variable;

public class ApplicationConstant {
    
    public static final String APP_NAME = "/ticketing";
    public static final int HTTP_CLIENT_CONNECTION_TIMEOUT = 20000;
    public static final int HTTP_CLIENT_READ_TIMEOUT = 20000;
    public static final int SOA_TIMEOUT = 5000;
    public static final int USER_IDLE_TIMEOUT = 15;

    public static final int REST_PROXY_HTTP_CLIENT_CONNECTION_TIMEOUT = 60000;
    public static final int REST_PROXY_HTTP_CLIENT_READ_TIMEOUT = 60000;

    public static final String BEAN_APP_CONFIG = "appconf";

    public static final String BEAN_DATASOURCE_TICKET = "datasource-ticketing";
    public static final String BEAN_JDBC_TICKET = "jdbc-ticketing";
    public static final String BEAN_TRANSACTION_MANAGER = "transactionManager";
    public static final String BEAN_TRANSACTION_TEMPLATE = "transaction-template";

    public static final String BEAN_REST_TEMPLATE = "rest-template";
    public static final String BEAN_REST_PROXY_TEMPLATE = "restProxy";

}
