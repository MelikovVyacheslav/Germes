package org.slavik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private final String url;
    private final String user;
    private final String password;
    private static Connection con;

    public ConnectionManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found");
        }
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successfully");
        } catch (SQLException e) {
            System.out.println("Connection failed");
        }
    }

    public Connection getConnection() {
        return con;
    }
}
