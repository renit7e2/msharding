package com.cn.msharding.jdbc.router.service;


import com.cn.msharding.jdbc.router.ShardingRouter;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/2/20.</pre>
 */
public interface IShardingRouterService {

    /**
     * query effective sharding routers by logic tableName
     *
     * @param tableName logic tableName
     * @return sharding routers
     */
    List<ShardingRouter> listRouterStrategy(String tableName);

    /**
     * query all effective need route logic tableName
     *
     * @return logic table names
     */
    List<String> listRouterTable();

    /**
     * query all effective routers
     *
     * @return sharding routers
     */
    List<ShardingRouter> listAllEnableRouter();

    void createTableIfNotExist();
}
