package com.cn.msharding.core.filter;


import com.cn.msharding.core.ShardingParam;
import com.cn.msharding.jdbc.router.ShardingRouter;

import java.util.List;

/**
 * filter chain
 * <pre>Created by Kaizen Xue</pre>
 */
public interface FilterChain {

    /**
     * do filter
     *
     * @param shardingParam
     * @param shardingRouters
     */
    void doFilter(ShardingParam shardingParam, List<ShardingRouter> shardingRouters);
}
