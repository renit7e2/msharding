package com.cn.msharding.core.plugin;

import org.apache.ibatis.plugin.Invocation;

/**
 * 拦截处理器
 * <pre>Created by Kaizen Xue.</pre>
 */
public interface ShardingInterceptorProcessor {

    /**
     * handle intercept method
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    Object handle(Invocation invocation) throws Throwable;
}
