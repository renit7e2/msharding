package com.cn.msharding.core.constant;

import lombok.NoArgsConstructor;

/**
 * <pre>Created by Kaizen Xue</pre>
 */
@NoArgsConstructor
public class ShardingRegexConstant {

    /**
     * 集合正则表达式  eg:{2,[4-5]} or {[4-5],2}express 2,4,5
     */
    public static final String CONLLECT_REGEX = "\\{((\\d+|\\[\\d+\\-\\d+\\]),)*(\\d+|\\[\\d+\\-\\d+\\])\\}";

    /**
     * 范围正则表达式    eg:[2-5] express 2,3,4,5
     */
    public static final String RANGE_REGEX = "\\[(\\d+\\-\\d+)\\]";

    /**
     * 数字正则表达式
     */
    public static final String NUM_REGEX = "\\d+";

    /**
     * 集合分割字符
     */
    public static final String COLLECT_SPLIT_CHAR = ",";

    /**
     * 范围连接字符
     */
    public static final String RANGE_CONNECT_CHAR = "-";

}
