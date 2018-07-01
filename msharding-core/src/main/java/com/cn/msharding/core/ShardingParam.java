package com.cn.msharding.core;

import com.cn.msharding.core.strategy.TableChooseStrategy;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <pre>Created by Kaizen Xue on 2018/4/25.</pre>
 */

public class ShardingParam<T> {
    /**
     * 分片表逻辑名称
     */
    @Getter
    private String loginTableName;

    /**
     * 分片参数
     */
    @Getter
    private T shardingParam;

    /**
     * 是否进行分片
     */
    @Getter
    private boolean isSharding = true;

    /**
     * 选择策略
     */
    @Setter
    @Getter
    private Class<? extends TableChooseStrategy> chooseStrategy;

    public ShardingParam buildLogicName(String logicTableName) {
        this.loginTableName = logicTableName;
        return this;
    }

    public ShardingParam buildSharding(boolean isSharding) {
        this.isSharding = isSharding;
        return this;
    }

    public ShardingParam buildShardingParam(T t) {
        this.shardingParam = t;
        return this;
    }

    @Override

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
