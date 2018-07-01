package com.cn.msharding.core.filter;


import com.cn.msharding.common.util.JsonUtil;
import com.cn.msharding.common.util.collection.CollectionHelpUtil;
import com.cn.msharding.core.ShardingParam;
import com.cn.msharding.jdbc.router.ShardingRouter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 参数匹配filter
 * <pre>Created by Kaizen Xue</pre>
 */
public class ParamMatchFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ShardingParam shardingParam, List<ShardingRouter> shardingRouters, FilterChain filterChain) {
        String shardingParamStr = JsonUtil.obj2Str(shardingParam.getShardingParam());
        ShardingRouter noneParamRouter = null;
        Map<String, List<ShardingRouter>> shardings = new HashedMap();
        for (ShardingRouter shardingRouter : shardingRouters) {
            String routerParam = shardingRouter.getShardingParam();
            if (StringUtils.isBlank(routerParam)) {
                noneParamRouter = shardingRouter;
                continue;
            }
            List<ShardingRouter> routers = shardings.get(routerParam.trim());
            if (routers == null) {
                routers = new ArrayList<>();
            }
            routers.add(shardingRouter);
            shardings.put(routerParam.trim(), routers);
        }
        List<ShardingRouter> effectiveRouters = shardings.get(shardingParamStr);
        List<ShardingRouter> canUseRouters = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(effectiveRouters)) {
            canUseRouters.addAll(effectiveRouters);
        } else {
            if (noneParamRouter != null) {
                canUseRouters.add(noneParamRouter);
            }
        }
        CollectionHelpUtil.removeOther(shardingRouters, canUseRouters);
        filterChain.doFilter(shardingParam, shardingRouters);
    }

    @Override
    public void destroy() {

    }
}
