package org.slavik;

import java.sql.Connection;

public class OpenCartManager {
    protected Connection con;

    public OpenCartManager(Connection connection) {
        this.con = connection;
    }
}
