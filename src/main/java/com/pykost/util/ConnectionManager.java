package com.pykost.util;

import com.pykost.exception.ConnectionManagerException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final DataSource dataSource = HikariCPDataSource.getDataSource();

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionManagerException(e);
        }
    }

}
