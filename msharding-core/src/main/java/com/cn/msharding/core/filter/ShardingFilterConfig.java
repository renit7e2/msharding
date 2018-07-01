package com.cn.msharding.core.filter;

import com.cn.msharding.common.util.AssertUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import java.util.Collections;
import java.util.Enumeration;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
public class ShardingFilterConfig implements FilterConfig {
    private FilterDef filterDef;
    private BeanFactory beanFactory;

    @Override
    public String getFilterName() {
        return StringUtils.isNotBlank(filterDef.getDisplayName()) ?
                filterDef.getDisplayName() :
                filterDef.getFilterClass().getSimpleName().substring(0, 1).toLowerCase()
                        + filterDef.getFilterClass().getSimpleName().substring(1);
    }

    @Override
    public String getInitParam(String paramName) {
        return filterDef.getInitParams().get(paramName);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(filterDef.getInitParams().keySet());
    }

    @Override
    public BeanFactory getSpringContext() {
        return this.beanFactory;
    }

    public ShardingFilterConfig beanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        return this;
    }

    public ShardingFilterConfig filterDef(FilterDef filterDef) {
        AssertUtil.notNull(filterDef, "filterDef can not be null");
        AssertUtil.notNull(filterDef.getFilterClass(), "filterClass can not be null");
        this.filterDef = filterDef;
        return this;
    }
}
