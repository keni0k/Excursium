package com.heroku.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

    private final Environment env;

    @Autowired
    public DatabaseConfig(Environment env) {
        this.env = env;
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();

        String url = "postgres://qmnjagkdtvydwo:8d065c8a74f33586eb8af68ff8263f7c1b4e8a10213cb1ec22e8037cd27c4168@ec2-54-225-113-161.compute-1.amazonaws.com:5432/dc8bu9fgr1ksnh";

        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url);

        return ds;
    }
}