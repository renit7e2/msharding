package com.cn.msharding.core.filter;

import com.cn.msharding.core.ShardingParam;
import com.cn.msharding.jdbc.router.ShardingRouter;

import java.util.List;

/**
 * sharding router filter
 * <pre>Created by Kaizen Xue</pre>
 */
public interface Filter {

    /**
     * init filter
     *
     * @param filterConfig
     */
    void init(FilterConfig filterConfig);

    /**
     * do filter
     *
     * @param shardingParam
     * @param shardingRouters
     * @param filterChain
     */
    void doFilter(ShardingParam shardingParam, List<ShardingRouter> shardingRouters, FilterChain filterChain);

    /**
     * destroy filter
     */
    void destroy();
}
