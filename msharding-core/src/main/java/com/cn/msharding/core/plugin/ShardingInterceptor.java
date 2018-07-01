package com.cn.msharding.core.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

/**
 * 分片拦截器
 * <p>基于mybatis 3.2.0</p>
 * <pre>Created by Kaizen Xue.</pre>
 */
@Intercepts({
        @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class}),
        @Signature(method = "parameterize", type = StatementHandler.class, args = {Statement.class}),
        @Signature(method = "batch", type = StatementHandler.class, args = {Statement.class}),
        @Signature(method = "update", type = StatementHandler.class, args = {Statement.class}),
        @Signature(method = "query", type = StatementHandler.class, args = {Statement.class, ResultHandler.class})})
public class ShardingInterceptor implements Interceptor {
    private static final String PREPARE_METHOD = "prepare";
    private static final String PARAMETERIZE_METHOD = "parameterize";
    private static ShardingInterceptorProcessor statementInterceptorProcessor = new StatementInterceptorProcessor();
    private static ShardingInterceptorProcessor sqlInterceptorProcessor = new SqlInterceptorProcessor();
    private static ShardingInterceptorProcessor paramInterceptProcessor = new ParamInterceptProcessor();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (PREPARE_METHOD.equals(invocation.getMethod().getName())) {
            return statementInterceptorProcessor.handle(invocation);
        }
        if (PARAMETERIZE_METHOD.equals(invocation.getMethod().getName())) {
            return paramInterceptProcessor.handle(invocation);
        }
        return sqlInterceptorProcessor.handle(invocation);
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
