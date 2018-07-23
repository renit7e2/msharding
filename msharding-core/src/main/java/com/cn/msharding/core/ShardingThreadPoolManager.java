package com.cn.msharding.core;

import com.cn.msharding.common.exception.ShardingRunException;
import com.cn.msharding.common.util.AssertUtil;
import com.cn.msharding.common.util.async.Async;
import com.cn.msharding.common.util.async.AsyncResultUtil;
import com.cn.msharding.common.util.async.ThreadPoolUtil;
import com.cn.msharding.core.constant.ShardingConstant;
import com.cn.msharding.core.returnresult.merge.ReturnMergeDispatcher;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * sharding thread pool manager
 * <pre>Created by Kaizen Xue on 2018/5/3.</pre>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShardingThreadPoolManager {

    private static class ShardingThreadPoolHolder {
        private static ShardingThreadPoolManager INSTANCE = new ShardingThreadPoolManager();
    }

    private ExecutorService executorService;

    public static ShardingThreadPoolManager getInstance() {
        return ShardingThreadPoolHolder.INSTANCE;
    }

    public void init(Properties properties) {
        String core_thread = properties.getProperty(ShardingConstant.SHARDING_CORE_THREAD, String.valueOf(ShardingConstant.DEFAULT_SHARDING_CORE_THREAD_NUM));
        String max_thread = properties.getProperty(ShardingConstant.SHARDING_MAX_THREAD, String.valueOf(ShardingConstant.DEFAULT_SHARDING_MAX_THREAD_NUM));
        String queueLength = properties.getProperty(ShardingConstant.SHARDING_QUEUE_LENGTH, String.valueOf(ShardingConstant.DEFALUT_SHARDING_QUEUE_LENGTH_NUM));
        executorService = new ThreadPoolExecutor(Integer.valueOf(core_thread), Integer.valueOf(max_thread), 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(Integer.valueOf(queueLength)), new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "sharding-exe-executor-" + threadNumber.getAndIncrement());
                if (!thread.isDaemon()) {
                    thread.setDaemon(true);
                }
                if (thread.getPriority() != Thread.MAX_PRIORITY) {
                    thread.setPriority(Thread.MAX_PRIORITY);
                }
                return thread;
            }
        }, new ThreadPoolExecutor.AbortPolicy());
    }

    public void shutdown() {
        if (executorService == null || !executorService.isShutdown()) {
            executorService.shutdown();
        }

    }

    public <T> Object multiInvoke(Invocation invocation, List<Statement> statements, Class<T> clazzType) {
        AssertUtil.notEmpty(statements, "sharding multi statements can not be empty");
        AssertUtil.notNull(clazzType, "sharding return  clazzType can not be null");
        try {
            final StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            final Method method = invocation.getMethod();
            final Object[] args = invocation.getArgs();
            List<Future<Object>> futures = new ArrayList<>();
            for (Statement statement : statements) {
                Future<Object> future = ThreadPoolUtil.asyncCall(executorService, statement, new Async<Object, Statement>() {
                    @Override
                    public Object run(Statement statement) throws Exception {
                        if (args.length == 2) {
                            return method.invoke(statementHandler, statement, args[1]);
                        }
                        return method.invoke(statementHandler, statement);
                    }
                });
                futures.add(future);
            }

            return ReturnMergeDispatcher.getInstance().merge(AsyncResultUtil.getResultList(futures), clazzType);
        } catch (Exception e) {
            log.error("sharding mybatis invoke exception", e);
            throw new ShardingRunException("sharding mybatis invoke exception", e);
        }
    }
}
