package com.cn.msharding.core.filter;

import com.cn.msharding.common.exception.ShardingException;
import com.cn.msharding.common.util.JsonUtil;
import com.cn.msharding.common.util.collection.CollectionHelpUtil;
import com.cn.msharding.common.util.collection.Property;
import com.cn.msharding.core.ShardingParam;
import com.cn.msharding.jdbc.router.ShardingRouter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 重复路由策略filter
 * <pre>
 *     去除掉数据库相同策略配置参数相同的filter(只保留一个)
 * </pre>
 * <pre>Created by Kaizen Xue</pre>
 */
@Slf4j
public class RepeatRouterFilter implements Filter {
    private static final String CONNECT_CHAR = "&";

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ShardingParam shardingParam, List<ShardingRouter> shardingRouters, FilterChain filterChain) {
        Set<String> strategies = CollectionHelpUtil.extractSet(shardingRouters, new Property<ShardingRouter, String>() {
            @Override
            public String gain(ShardingRouter shardingRouter) {
                return shardingRouter.getTableStrategy();
            }
        });

        if (CollectionUtils.isNotEmpty(shardingRouters) && shardingRouters.size() != strategies.size()) {
            Set<String> exists = new HashSet<>();
            Iterator<ShardingRouter> iterator = shardingRouters.iterator();
            while (iterator.hasNext()) {
                ShardingRouter router = iterator.next();
                Object object = JsonUtil.str2Obj(router.getShardingParam(), Object.class);
                if (object == null) {
                    log.error("illegal sharding param,this strategy will be removed,router={}", router);
                    throw new ShardingException("illegal sharding param,router id" + router.getId());
                }
                String json = JsonUtil.obj2Str(object);
                if (json == null) {
                    log.error("illegal sharding param,this strategy will be removed,router={}", router);
                    throw new ShardingException("illegal sharding param,router id" + router.getId());
                }
                String key = router.getTableStrategy() + CONNECT_CHAR + json;
                if (exists.contains(key)) {
                    iterator.remove();
                }
                exists.add(key);
            }
        }
        filterChain.doFilter(shardingParam, shardingRouters);
    }

    @Override
    public void destroy() {

    }
}
