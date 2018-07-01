package com.cn.msharding.core.builder;

import com.cn.msharding.common.util.AssertUtil;
import com.cn.msharding.core.plugin.ShardingStatementHandler;
import javafx.util.Builder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
public class ShardingInvokerBuilder implements Builder<ShardingInvokerBuilder.ShardingInvoker> {
    private Invocation invocation;

    private MappedStatement mappedStatement;

    private String logicName;

    private String realTableName;

    public ShardingInvokerBuilder buildInvocation(Invocation invocation) {
        this.invocation = invocation;
        return this;
    }

    public ShardingInvokerBuilder buildMappedStatement(MappedStatement mappedStatement) {
        this.mappedStatement = mappedStatement;
        return this;
    }

    public ShardingInvokerBuilder buildLogicName(String logicName) {
        this.logicName = logicName;
        return this;
    }

    public ShardingInvokerBuilder buildTableName(String tableName) {
        this.realTableName = tableName;
        return this;
    }

    @Override
    public ShardingInvoker build() {
        AssertUtil.notNull(invocation, "sharding invocation can not be null");
        AssertUtil.notBlank(logicName, "logic name can not be blank");
        AssertUtil.notBlank(realTableName, "real table name can not be blank");
        AssertUtil.notNull(mappedStatement, "harding mappedStatement can not be null");
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        String replaceSql = sql.replace(this.logicName, this.realTableName);
        BoundSql replaceBoundSql = new BoundSql(this.mappedStatement.getConfiguration(), replaceSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        ShardingStatementHandler shardingStatementHandler = new ShardingStatementHandler(statementHandler, replaceBoundSql, mappedStatement, mappedStatement.getConfiguration());
        return new ShardingInvoker(new Invocation(shardingStatementHandler, this.invocation.getMethod(), this.invocation.getArgs()));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public class ShardingInvoker {
        @Getter
        private Invocation invocation;
    }
}
