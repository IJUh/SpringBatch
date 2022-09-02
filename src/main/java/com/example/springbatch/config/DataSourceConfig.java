package com.example.springbatch.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create().build();
    }
}
