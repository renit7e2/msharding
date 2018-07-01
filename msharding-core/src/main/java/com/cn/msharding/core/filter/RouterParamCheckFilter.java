package com.cn.msharding.core.filter;

import com.cn.msharding.common.util.AssertUtil;
import com.cn.msharding.core.ShardingParam;
import com.cn.msharding.jdbc.router.ShardingRouter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
public class RouterParamCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ShardingParam shardingParam, List<ShardingRouter> shardingRouters, FilterChain filterChain) {
        if (CollectionUtils.isNotEmpty(shardingRouters)) {
            for (ShardingRouter router : shardingRouters) {
                long routerId = router.getId();
                AssertUtil.notBlank(router.getTableName(), "sharding tableName can not be blank,routerId=" + routerId);
                AssertUtil.notBlank(router.getTableStrategy(), "sharding startegy can not be blank,routerId=" + routerId);
                AssertUtil.notBlank(router.getTableRegex(), "sharding table regex can not be blank,routerId=" + routerId);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
