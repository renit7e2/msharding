package com.cn.msharding.core;

import com.cn.msharding.common.exception.ShardingException;
import com.cn.msharding.common.util.AssertUtil;
import com.cn.msharding.common.util.bean.BeanCopyUtil;
import com.cn.msharding.core.algorithm.ShardingAlgorithm;
import com.cn.msharding.core.filter.ShardingFilterManager;
import com.cn.msharding.core.regex.ShardingRegexResolver;
import com.cn.msharding.core.strategy.ShardingAlgorithmConfiguration;
import com.cn.msharding.jdbc.router.ShardingRouter;
import com.cn.msharding.jdbc.router.service.IShardingRouterService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.concurrent.*;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
@Slf4j
public class ShardingContext implements BeanFactoryAware, InitializingBean {
    @Getter
    private BeanFactory beanFactory;

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * the logicTables need shardinged
     */
    private volatile Set<String> logicTables = new HashSet<>();
    /**
     * sharding routers <logicTableName,sharding routers>
     */
    private volatile Map<String, List<ShardingRouter>> shardingRouters = new HashMap<>();

    /**
     * sharding regex express tables
     */
    private volatile Map<String, List<String>> regexTables = new HashMap<>();

    /**
     * sharding algorithms <shardingStrategy,ShardingAlgorithm>
     */
    private Map<String, ShardingAlgorithm> shardingAlgorithmMap = new HashMap<>();
    @Setter
    private ShardingAlgorithmConfiguration shardingAlgorithmConfiguration;

    /**
     * init status
     */
    private static volatile boolean isInit = false;

    private static ShardingContext INSTANCE;

    public static ShardingContext getInstance() {
        if (!isInit) {
            throw new ShardingException("the mharding context is not initialized");
        }
        return INSTANCE;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        ShardingFilterManager.getInstance().init(beanFactory);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
        this.shardingAlgorithmMap = this.shardingAlgorithmConfiguration.registerAlgorithm();
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "msharding-refresh-executor");

            }
        }, new ThreadPoolExecutor.AbortPolicy());
        init();
        isInit = true;
    }

    private void init() {
        //主动刷新需要路由的表
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    IShardingRouterService shardingRouterService = beanFactory.getBean(IShardingRouterService.class);
                    List<ShardingRouter> routers = shardingRouterService.listAllEnableRouter();
                    Map<String, List<String>> tableMap = new HashMap<>();
                    if (CollectionUtils.isNotEmpty(routers)) {
                        Map<String, List<ShardingRouter>> shardingRouterMap = new HashMap<>();
                        for (ShardingRouter router : routers) {
                            String logicTableName = router.getTableName().trim();
                            AssertUtil.notBlank(logicTableName, "logic tableName can not be blank");
                            List<ShardingRouter> routerStrategies = shardingRouterMap.get(logicTableName);
                            if (routerStrategies == null) {
                                routerStrategies = new ArrayList<>();
                            }
                            routerStrategies.add(router);
                            shardingRouterMap.put(logicTableName, routerStrategies);
                            List<String> tables = ShardingRegexResolver.resolving(router.getTableRegex().trim());
                            tableMap.put(router.getTableRegex().trim(), tables);
                        }
                        logicTables = shardingRouterMap.keySet();
                        shardingRouters = shardingRouterMap;
                        regexTables = tableMap;
                    }
                } catch (Exception e) {
                    log.error("refresh routing config exception......", e);
                }
                log.info("refresh sharding routers...");
            }

        }, 0, 1, TimeUnit.MINUTES);
    }

    public ShardingAlgorithm getShardingAlgorithm(String strategy) {
        AssertUtil.notBlank(strategy, "sharding strategy can not be blank");
        ShardingAlgorithm shardingAlgorithm = shardingAlgorithmMap.get(strategy.trim());
        if (null == shardingAlgorithm) {
            throw new ShardingException("sharding strategy not exist,strategy=" + strategy);
        }
        return shardingAlgorithm;
    }

    public List<ShardingRouter> getShardingRouters(String logicTableName) {
        AssertUtil.notBlank(logicTableName, "logic table name can not be blank");
        List<ShardingRouter> shardingRouters = this.shardingRouters.get(logicTableName.trim());
        if (CollectionUtils.isEmpty(shardingRouters)) {
            return new ArrayList<>();
        }
        return BeanCopyUtil.listCopy(shardingRouters, ShardingRouter.class);
    }

    public List<String> getShardingTables(String shardingRegex) {
        AssertUtil.notBlank(shardingRegex, "sharding regex can not be blank,shardingRegex=" + shardingRegex);
        return this.regexTables.get(shardingRegex.trim());
    }


    public void shutDown() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
    }
}
