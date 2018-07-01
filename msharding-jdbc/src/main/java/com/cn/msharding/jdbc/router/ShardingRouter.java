package com.cn.msharding.jdbc.router;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
@Data
@NoArgsConstructor
public class ShardingRouter implements Serializable, Comparable<ShardingRouter> {

    /**
     * 是否有效标志
     */
    public static final int ENABLE_FLAG = 1;

    private long id;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 分表策略
     */
    private String tableStrategy;
    /**
     * 分片参数
     */
    private String shardingParam;
    /**
     * 分表正则表达式
     */
    private String tableRegex;
    /**
     * 分表算法优先级（数字越小优先级越高）
     */
    private int order;
    /**
     * 是否生效：0无效，1有效
     * {@link #ENABLE_FLAG}
     */
    private int enable;

    private Date createTime;

    private Date updateTime;

    @Override
    public int compareTo(ShardingRouter o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
