package com.cn.msharding.core;

import com.cn.msharding.core.algorithm.ShardingAlgorithm;
import com.cn.msharding.core.constant.ShardingConstant;
import com.cn.msharding.jdbc.router.ShardingRouter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/4/28.</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShardingHelper {

    /**
     * 计算路由表名
     *
     * @param logicName       logic tableName
     * @param shardingRouters sharding routers
     * @param shardingParam   sharding param
     * @param <T>             sharding param type
     * @return real table names
     */
    public final static <T> List<String> calRouterTableName(String logicName, List<ShardingRouter> shardingRouters, ShardingParam<T> shardingParam) {
        List<String> preNames = new ArrayList<>();
        preNames.add(logicName);
        for (ShardingRouter shardingRouter : shardingRouters) {
            preNames = calTableName(preNames, shardingRouter.getTableRegex(), shardingParam,
                    ShardingContext.getInstance().getShardingAlgorithm(shardingRouter.getTableStrategy()));
        }
        return preNames;
    }


    private final static <T> List<String> calTableName(List<String> preNames, String tableRegex, ShardingParam<T> shardingParam, ShardingAlgorithm<T> shardingAlgorithm) {
        List<String> result = new ArrayList<>();
        for (String preName : preNames) {
            List<String> tables = calTableName(preName, tableRegex, shardingParam, shardingAlgorithm);
            result.addAll(tables);
        }
        return result;
    }

    private final static <T> List<String> calTableName(String preName, String tableRegex, ShardingParam<T> shardingParam, ShardingAlgorithm<T> shardingAlgorithm) {
        return calTableName(preName, ShardingContext.getInstance().getShardingTables(tableRegex), shardingParam, shardingAlgorithm);
    }


    private final static <T> List<String> calTableName(String preName, List<String> availableTables, ShardingParam<T> shardingParam, ShardingAlgorithm<T> shardingAlgorithm) {
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(availableTables)) {
            result.add(preName);
            return result;
        }
        List<String> _post = shardingAlgorithm.doSharding(availableTables, shardingParam);
        if (CollectionUtils.isEmpty(_post)) {
            result.add(preName);
            return result;
        }
        for (String table : _post) {
            result.add(preName + ShardingConstant.SHARDING_TABLE_CONNECT_CHAR + table);
        }
        return result;
    }
}
