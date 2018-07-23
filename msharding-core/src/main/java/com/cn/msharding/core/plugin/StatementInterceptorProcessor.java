package com.cn.msharding.core.plugin;

import com.cn.msharding.common.exception.ShardingRunException;
import com.cn.msharding.common.util.ReflectUtil;
import com.cn.msharding.core.ShardingContext;
import com.cn.msharding.core.ShardingHelper;
import com.cn.msharding.core.ShardingParam;
import com.cn.msharding.core.annotion.TableChoose;
import com.cn.msharding.core.builder.ShardingInvokerBuilder;
import com.cn.msharding.core.filter.FilterChain;
import com.cn.msharding.core.filter.ShardingFilterManager;
import com.cn.msharding.core.strategy.AllTableChooseStrategy;
import com.cn.msharding.core.strategy.TableChooseStrategy;
import com.cn.msharding.jdbc.router.ShardingRouter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.*;

/**
 * statement拦截处理器
 * <pre>Created by Kaizen Xue.</pre>
 */
@Slf4j
public class StatementInterceptorProcessor implements ShardingInterceptorProcessor {
    //<mapperId,chooseStrategy>
    private volatile Map<String, TableChooseStrategy> tableChooseStrategyMap = new HashMap<>();
    //<mapperId,returnType>
    private volatile Map<String, Class<?>> returnTypeMap = new HashedMap();

    @Override
    public Object handle(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        BoundSql boundSql = statementHandler.getBoundSql();

        Object obj = boundSql.getParameterObject();

        ShardingParam shardingParam = getShardingParam(obj);
        //不需要分表的对象直接执行
        if (null == shardingParam) {
            return invocation.proceed();
        }

        if (!shardingParam.isSharding()) {
            return invocation.proceed();
        }
        String loginTableName = shardingParam.getLoginTableName();
        List<ShardingRouter> shardingRouters = ShardingContext.getInstance().getShardingRouters(loginTableName);

        FilterChain filterChain = ShardingFilterManager.getInstance().builder().build();
        filterChain.doFilter(shardingParam, shardingRouters);
        //没有有效的路由配置,执行原表
        if (CollectionUtils.isEmpty(shardingRouters)) {
            log.debug("sharding filter execute,don't have effective sharding routers,logic table name {}", loginTableName);
            return invocation.proceed();
        }

        DefaultParameterHandler parameterHandler = (DefaultParameterHandler) statementHandler.getParameterHandler();
        Field field = DefaultParameterHandler.class.getDeclaredField("mappedStatement");
        field.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) field.get(parameterHandler);

        String mapperId = mappedStatement.getId();
        TableChooseStrategy tableChooseStrategy = tableChooseStrategyMap.get(mapperId);
        if (tableChooseStrategy == null) {
            String className = mapperId.substring(0, mapperId.lastIndexOf("."));
            String methodName = mapperId.substring(mapperId.lastIndexOf(".") + 1, mapperId.length());
            Class<?> clazz = Class.forName(className);
            Method method = ReflectUtil.getMethod(clazz, methodName);
            TableChoose tableChoose = method.getAnnotation(TableChoose.class);
            Class<? extends TableChooseStrategy> chooseStrategy = AllTableChooseStrategy.class;
            if (tableChoose != null && tableChoose.value() != null) {
                chooseStrategy = tableChoose.value();
            }
            tableChooseStrategy = ReflectUtil.newInstance(chooseStrategy);
            tableChooseStrategyMap.put(mapperId, tableChooseStrategy);
            returnTypeMap.put(mapperId, method.getReturnType());
        }

        List<String> realTableNames = getShardingRealTableName(loginTableName, shardingRouters, shardingParam, tableChooseStrategy);
        if (CollectionUtils.isEmpty(realTableNames)) {
            return invocation.proceed();
        }

        List<Statement> statements = new ArrayList<>(realTableNames.size());
        for (String realName : realTableNames) {
            ShardingInvokerBuilder.ShardingInvoker invoker = new ShardingInvokerBuilder().
                    buildInvocation(invocation).
                    buildMappedStatement(mappedStatement).
                    buildLogicName(loginTableName).
                    buildTableName(realName).
                    build();
            statements.add((Statement) invoker.getInvocation().proceed());
        }
        return new ShardingStatement(statements, returnTypeMap.get(mapperId));
    }

    private ShardingParam getShardingParam(Object param) {
        if (param == null) {
            return null;
        }
        if (param instanceof ShardingParam) {
            return (ShardingParam) param;
        }
        if (param instanceof Map) {
            try {
                Object object = ((Map) param).get("sharding");
                if (object instanceof ShardingParam) {
                    return (ShardingParam) object;
                }
            } catch (BindingException e) {
                //ignore this exception,doNothing
            }
            for (Object item : ((Map) param).values()) {
                if (item instanceof ShardingParam) {
                    return (ShardingParam) item;
                }
            }
        }
        return null;
    }

    /**
     * 获取分片真实表名
     *
     * @param logicName       逻辑表名
     * @param shardingRouters 分片路由
     * @param shardingParam   分片参数
     * @return 分片结果
     */
    private List<String> getShardingRealTableName(String logicName, List<ShardingRouter> shardingRouters, ShardingParam shardingParam, TableChooseStrategy tableChooseStrategy) {

        Collections.sort(shardingRouters);
        List tables = ShardingHelper.calRouterTableName(logicName, shardingRouters, shardingParam);

        List<String> realTableNames = new ArrayList<>();
        //根据选择策略执行返回执行表
        Object chooseResult = tableChooseStrategy.choose(tables);
        if (chooseResult instanceof List) {
            return (List) chooseResult;
        }
        if (chooseResult instanceof String) {
            realTableNames.add((String) chooseResult);
            return realTableNames;
        }
        throw new ShardingRunException("illegal sharding strategy returnresult type,strategy=" + tableChooseStrategy.getClass().getSimpleName());
    }
}
