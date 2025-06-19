package org.slavik;

import org.springframework.jdbc.datasource.DriverManagerDataSource;


import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private final String url;
    private final String user;
    private final String password;
    private DriverManagerDataSource con;

    public ConnectionManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void connection() throws SQLException {
        con = new DriverManagerDataSource();
        con.setDriverClassName("com.mysql.cj.jdbc.Driver");
        con.setUrl(url);
        con.setUsername(user);
        con.setPassword(password);
    }

    public Connection getConnection() throws SQLException {
        return con.getConnection();
    }
}
