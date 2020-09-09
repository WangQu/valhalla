package com.iflytek.zhyl.valhalla.config;

import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SpringSqlCloseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;

@Configuration
public class QuerydslAutoConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SQLQueryFactory queryFactory() {
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(PostgreSQLTemplates.builder().build());
        configuration.addListener(new SpringSqlCloseListener(dataSource));
        return new SQLQueryFactory(configuration, () -> DataSourceUtils.getConnection(dataSource));
    }
}

