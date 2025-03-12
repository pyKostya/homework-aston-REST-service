package com.pykost.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikariCPDataSource {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        config.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        config.setUsername(PropertiesUtil.get(USERNAME_KEY));
        config.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        dataSource = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
