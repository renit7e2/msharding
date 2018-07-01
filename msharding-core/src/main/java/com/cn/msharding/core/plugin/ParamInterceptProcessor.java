package com.cn.msharding.core.plugin;

import org.apache.ibatis.plugin.Invocation;

import java.sql.Statement;
import java.util.List;

/**
 * 参数拦截处理器
 * <pre>Created by Kaizen Xue.</pre>
 */
public class ParamInterceptProcessor implements ShardingInterceptorProcessor {
    @Override
    public Object handle(Invocation invocation) throws Throwable {
        Statement statement = (Statement) invocation.getArgs()[0];
        if (statement instanceof ShardingStatement) {
            ShardingStatement shardingStatement = (ShardingStatement) statement;
            List<Statement> statements = shardingStatement.getStatements();
            for (Statement st : statements) {
                invocation.getMethod().invoke(invocation.getTarget(), st);
            }
        }
        return invocation.proceed();
    }
}
