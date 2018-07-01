package com.cn.msharding.core.filter;

import com.cn.msharding.common.util.collection.CollectionHelpUtil;
import com.cn.msharding.common.util.collection.Predicate;
import com.cn.msharding.core.ShardingParam;
import com.cn.msharding.jdbc.router.ShardingRouter;

import java.util.List;

/**
 * 有效路由filter
 * <pre>Created by Kaizen Xue</pre>
 */
public class EnableRouterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ShardingParam shardingParam, List<ShardingRouter> shardingRouters, FilterChain filterChain) {
        CollectionHelpUtil.filter(shardingRouters, new Predicate<ShardingRouter>() {
            @Override
            public boolean evaluate(ShardingRouter shardingRouter) {
                return shardingRouter.getEnable() == ShardingRouter.ENABLE_FLAG;
            }
        });
        filterChain.doFilter(shardingParam, shardingRouters);
    }

    @Override
    public void destroy() {

    }
}
