package com.cn.msharding.core.plugin;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;

import java.sql.*;
import java.util.List;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
public class ShardingStatementHandler implements StatementHandler {
    private StatementHandler statementHandler;
    private BoundSql shardingBoundSql;
    private MappedStatement mappedStatement;
    private Configuration configuration;

    public ShardingStatementHandler(StatementHandler statementHandler, BoundSql shardingBoundSql, MappedStatement mappedStatement, Configuration configuration) {
        this.statementHandler = statementHandler;
        this.shardingBoundSql = shardingBoundSql;
        this.mappedStatement = mappedStatement;
        this.configuration = configuration;
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        ErrorContext.instance().sql(shardingBoundSql.getSql());
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            setStatementTimeout(statement);
            setFetchSize(statement);
            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
            throw e;
        } catch (Exception e) {
            closeStatement(statement);
            throw new ExecutorException("Error preparing statement. Cause: " + e, e);
        }
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        if (statement instanceof ShardingStatement) {
            ShardingStatement shardingStatement = (ShardingStatement) statement;
            List<Statement> statements = shardingStatement.getStatements();
            for (Statement st : statements) {
                statementHandler.parameterize(st);
            }
            return;
        }
        statementHandler.parameterize(statement);
    }

    @Override
    public void batch(Statement statement) throws SQLException {
        statementHandler.batch(statement);
    }

    @Override
    public int update(Statement statement) throws SQLException {
        return statementHandler.update(statement);
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        return statementHandler.query(statement, resultHandler);
    }

    @Override
    public BoundSql getBoundSql() {
        return this.shardingBoundSql;
    }

    @Override
    public ParameterHandler getParameterHandler() {
        return statementHandler.getParameterHandler();
    }

    private void setStatementTimeout(Statement stmt) throws SQLException {
        Integer timeout = mappedStatement.getTimeout();
        Integer defaultTimeout = configuration.getDefaultStatementTimeout();
        if (timeout != null) {
            stmt.setQueryTimeout(timeout);
        } else if (defaultTimeout != null) {
            stmt.setQueryTimeout(defaultTimeout);
        }
    }

    private void setFetchSize(Statement stmt) throws SQLException {
        Integer fetchSize = mappedStatement.getFetchSize();
        if (fetchSize != null) {
            stmt.setFetchSize(fetchSize);
        }
    }

    private void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            //ignore
        }
    }

    private Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = shardingBoundSql.getSql();
        if (mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator) {
            String[] keyColumnNames = mappedStatement.getKeyColumns();
            if (keyColumnNames == null) {
                return connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            } else {
                return connection.prepareStatement(sql, keyColumnNames);
            }
        } else if (mappedStatement.getResultSetType() != null) {
            return connection.prepareStatement(sql, mappedStatement.getResultSetType().getValue(), ResultSet.CONCUR_READ_ONLY);
        } else {
            return connection.prepareStatement(sql);
        }
    }
}
