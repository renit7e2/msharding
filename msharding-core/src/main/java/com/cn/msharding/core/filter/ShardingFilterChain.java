package com.cn.msharding.core.filter;

import com.cn.msharding.core.ShardingParam;
import com.cn.msharding.jdbc.router.ShardingRouter;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
public final class ShardingFilterChain implements FilterChain {
    @Getter
    private List<Filter> filters;
    private int position = 0;
    private int filterNum = 0;

    @Override
    public void doFilter(ShardingParam shardingParam, List<ShardingRouter> shardingRouters) {
        innerDoFilter(shardingParam, shardingRouters);
    }


    private void innerDoFilter(ShardingParam shardingParam, List<ShardingRouter> shardingRouters) {
        if (CollectionUtils.isEmpty(filters)) {
            return;
        }
        if (position < filterNum) {
            Filter filter = filters.get(position);
            position++;
            filter.doFilter(shardingParam, shardingRouters, this);
        }
    }

    protected void addFilter(Filter filter) {
        if (null == filters) {
            this.filters = new ArrayList<>();
            this.position = 0;
        }
        filters.add(filter);
        filterNum = filters.size();
    }

    protected void release() {
        position = 0;
        filterNum = 0;
        if (filters != null) {
            filters.clear();
        }
    }
}

