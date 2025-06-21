package org.slavik;

import org.springframework.jdbc.datasource.DriverManagerDataSource;


import javax.sql.DataSource;
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

    public DataSource connection() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        // Сохраняем созданный DataSource в поле класса
        this.con = dataSource;

        // Возвращаем DataSource
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        return con.getConnection();
    }
}
