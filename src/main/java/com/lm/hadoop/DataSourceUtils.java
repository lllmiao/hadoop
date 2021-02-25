package com.lm.hadoop;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.io.IOException;


public class DataSourceUtils {
    private static DataSource dataSource;

    public static DataSource getDataSource(String driverClassName, String url, String password) throws IOException {
        dataSource = DataSourceBuilder
                .create()
                .driverClassName(driverClassName)
                .url(url)
                .password(password)
                .build();

        return dataSource;
    }


}
