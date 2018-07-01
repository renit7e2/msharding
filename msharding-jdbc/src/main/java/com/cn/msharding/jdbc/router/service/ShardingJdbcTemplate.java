package com.cn.msharding.jdbc.router.service;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by kaizen on 2018/6/30.
 */
public class ShardingJdbcTemplate extends JdbcTemplate {
    public ShardingJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }
}
