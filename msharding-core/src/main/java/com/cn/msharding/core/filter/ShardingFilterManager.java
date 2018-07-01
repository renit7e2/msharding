package com.cn.msharding.core.filter;

import com.cn.msharding.common.exception.ShardingException;
import com.cn.msharding.core.constant.ShardingConstant;
import javafx.util.Builder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.BeanFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>Created by Kaizen Xue</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShardingFilterManager {
    private BeanFactory beanFactory;

    private List<Filter> filters;

    private Map<String, FilterDef> customFilters = new HashMap<>();

    private volatile boolean isFilterDone = false;

    private static class ManagerHolder {
        private static ShardingFilterManager INSTANCE = new ShardingFilterManager();

    }

    public static ShardingFilterManager getInstance() {
        return ManagerHolder.INSTANCE;
    }

    /**
     * init method
     *
     * @param beanFactory
     */
    public void init(BeanFactory beanFactory) {
        registerDefaultFilter();
        this.beanFactory = beanFactory;
    }


    /**
     * 注册filter
     *
     * @param filterName filter Name
     * @param clazz      filter
     * @param params     params<参数名,参数值>
     * @return
     */
    public void register(String filterName, Class<? extends Filter> clazz, Map<String, String> params) {
        FilterDef filterDef = customFilters.get(filterName);
        if (null == filterDef) {
            filterDef = new FilterDef().displayName(filterName).filterClass(clazz);
            if (MapUtils.isNotEmpty(params)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    filterDef.addParam(entry.getKey(), entry.getValue());
                }
            }
        }
        customFilters.put(filterName, filterDef);
    }


    private void registerDefaultFilter() {
        register(ShardingConstant.PARAM_CHECK_ROUTER, RouterParamCheckFilter.class, MapUtils.EMPTY_MAP);
        register(ShardingConstant.ENABLE_FILTER, EnableRouterFilter.class, MapUtils.EMPTY_MAP);
        register(ShardingConstant.REPEAT_ROUTER_FILTER, RepeatRouterFilter.class, MapUtils.EMPTY_MAP);
        register(ShardingConstant.PARAM_MATCH_FILTER, ParamMatchFilter.class, MapUtils.EMPTY_MAP);
    }


    /**
     * destroy method
     */
    public void destroy() {
        if (CollectionUtils.isNotEmpty(filters)) {
            for (Filter filter : filters) {
                filter.destroy();
            }
        }
    }

    public FilterBuilder builder() {
        return new FilterBuilder();
    }

    public final class FilterBuilder implements Builder<FilterChain> {

        @Override
        public FilterChain build() {
            ShardingFilterChain shardingFilterChain = new ShardingFilterChain();
            for (FilterDef filterDef : customFilters.values()) {
                try {
                    Filter filter = filterDef.getFilterClass().newInstance();
                    filter.init(new ShardingFilterConfig().filterDef(filterDef).beanFactory(beanFactory));
                    shardingFilterChain.addFilter(filter);
                } catch (Exception e) {
                    throw new ShardingException("reflect filter error", e);
                }
            }
            if (!isFilterDone) {
                filters = shardingFilterChain.getFilters();
                isFilterDone = true;
            }
            return shardingFilterChain;
        }
    }
}
