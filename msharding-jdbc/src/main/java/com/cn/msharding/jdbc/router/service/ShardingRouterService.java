package com.cn.msharding.jdbc.router.service;

import com.cn.msharding.jdbc.router.ShardingRouter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/2/20.</pre>
 */
public class ShardingRouterService implements IShardingRouterService, InitializingBean {
    @Autowired
    private ShardingJdbcTemplate shardingJdbcTemplate;
    private volatile boolean init = false;

    @Override
    public List<ShardingRouter> listRouterStrategy(String tableName) {
        Object[] paramValues = new Object[]{tableName, ShardingRouter.ENABLE_FLAG};
        String sql = "select * from t_sharding_router where table_name = ? and enable = ?";
        return shardingJdbcTemplate.query(sql, paramValues, new ShardingRouterMapper());
    }

    @Override
    public List<String> listRouterTable() {
        Object[] paramValues = new Object[]{ShardingRouter.ENABLE_FLAG};
        String sql = "select distinct(table_name) from t_sharding_router where enable = ?";
        return shardingJdbcTemplate.queryForList(sql, paramValues, String.class);
    }

    @Override
    public List<ShardingRouter> listAllEnableRouter() {
        Object[] paramValues = new Object[]{ShardingRouter.ENABLE_FLAG};
        String sql = "select * from t_sharding_router where enable = ?";
        return shardingJdbcTemplate.query(sql, paramValues, new ShardingRouterMapper());
    }


    @Override
    public void createTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS `t_sharding_router` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `table_name` varchar(50) NOT NULL COMMENT '表名',\n" +
                "  `table_strategy` varchar(50) NOT NULL COMMENT '分表策略',\n" +
                "  `sharding_param` varchar(100) NOT NULL COMMENT '分片参数(json形式)',\n" +
                "  `table_regex` varchar(50) NOT NULL COMMENT '分表正则表达式',\n" +
                "  `order` tinyint(4) NOT NULL COMMENT '分表算法优先级（数字越小优先级越高）',\n" +
                "  `enable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否生效：0无效，1有效',\n" +
                "  `create_time` datetime NOT NULL,\n" +
                "  `update_time` datetime NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `idx_sharding` (`table_name`,`table_strategy`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分片路由表';";
        shardingJdbcTemplate.execute(sql);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public void init() {
        if (!init) {
            createTableIfNotExist();
            init = true;
        }
    }

    private class ShardingRouterMapper implements RowMapper<ShardingRouter> {
        @Override
        public ShardingRouter mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShardingRouter router = new ShardingRouter();
            router.setId(rs.getLong("id"));
            router.setTableName(rs.getString("table_name"));
            router.setTableStrategy(rs.getString("table_strategy"));
            router.setShardingParam(rs.getString("sharding_param"));
            router.setTableRegex(rs.getString("table_regex"));
            router.setOrder(rs.getInt("order"));
            router.setEnable(rs.getInt("enable"));
            router.setCreateTime(rs.getTime("create_time"));
            router.setUpdateTime(rs.getTime("update_time"));
            return router;
        }
    }
}
