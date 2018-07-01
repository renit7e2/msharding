package com.cn.msharding.core.filter;

import org.springframework.beans.factory.BeanFactory;

import java.util.Enumeration;

/**
 * <pre>Created by Kaizen Xue</pre>
 */
public interface FilterConfig {
    /**
     * get filter name
     *
     * @return filter name
     */
    String getFilterName();

    /**
     * get initialized param value by param name
     *
     * @param paramName
     * @return param value
     */
    String getInitParam(String paramName);

    /**
     * get initialized param names
     *
     * @return paramNames
     */
    Enumeration<String> getInitParameterNames();

    /**
     * get spring bean factory
     *
     * @return
     */
    BeanFactory getSpringContext();
}
