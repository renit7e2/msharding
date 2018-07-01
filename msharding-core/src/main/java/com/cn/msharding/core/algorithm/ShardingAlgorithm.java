package com.cn.msharding.core.algorithm;


import com.cn.msharding.core.ShardingParam;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/2/21.</pre>
 */
public interface ShardingAlgorithm<T> {
    /**
     * 分片
     *
     * @param availableTables 可用表
     * @param param           分片参数
     * @return 分片选择表
     */
    List<String> doSharding(List<String> availableTables, ShardingParam<T> param);
}
