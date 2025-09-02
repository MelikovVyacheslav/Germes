package org.slavik;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ConnectionManager {
    private final String url;
    private final String user;
    private final String password;
    private HikariDataSource dataSource = null;

    public ConnectionManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(300000);
        config.setIdleTimeout(6000000);
        config.setMaxLifetime(180000000);
        dataSource = new HikariDataSource(config);
        return dataSource;
    }

    public void disconnect() {
        dataSource.close();
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
