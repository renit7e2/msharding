package com.cn.msharding.core.plugin;

import com.cn.msharding.core.ShardingThreadPoolManager;
import org.apache.ibatis.plugin.Invocation;

import java.sql.Statement;

/**
 * sql拦截处理器
 * <pre>Created by Kaizen Xue.</pre>
 */
public class SqlInterceptorProcessor implements ShardingInterceptorProcessor {
    @Override
    public Object handle(Invocation invocation) throws Throwable {
        Statement statement = (Statement) invocation.getArgs()[0];
        if (statement instanceof ShardingStatement) {
            ShardingStatement shardingStatement = (ShardingStatement) statement;
            return ShardingThreadPoolManager.getInstance().multiInvoke(invocation, shardingStatement.getStatements(), shardingStatement.getReturnType());
        }
        return invocation.proceed();
    }
}
