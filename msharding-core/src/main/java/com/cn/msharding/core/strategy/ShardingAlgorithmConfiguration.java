package com.cn.msharding.core.strategy;


import com.cn.msharding.core.algorithm.ShardingAlgorithm;

import java.util.Map;

/**
 * 分片算法注册配置接口
 * <pre>Created by Kaizen Xue on 2018/2/21.</pre>
 */
public interface ShardingAlgorithmConfiguration {
    /**
     * 注册分片算法
     *
     * @return <分片策略名称,分片算法>
     */
    Map<String, ShardingAlgorithm> registerAlgorithm();
}
