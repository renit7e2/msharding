package com.cn.msharding.core.constant;

import com.cn.msharding.core.filter.EnableRouterFilter;
import com.cn.msharding.core.filter.ParamMatchFilter;
import com.cn.msharding.core.filter.RepeatRouterFilter;
import com.cn.msharding.core.filter.RouterParamCheckFilter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * sharding constant class
 * <pre>Created by Kaizen Xue</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShardingConstant {
    /*************** default filter name start*******************/

    /**
     * @see RouterParamCheckFilter
     */
    public static final String PARAM_CHECK_ROUTER = "paramCheckFilter";

    /**
     * @see EnableRouterFilter
     */
    public static final String ENABLE_FILTER = "enableFilter";

    /**
     * @see RepeatRouterFilter
     */
    public static final String REPEAT_ROUTER_FILTER = "repeatFilter";

    /**
     * @see ParamMatchFilter
     */
    public static final String PARAM_MATCH_FILTER = "paramMatchFilter";
    /*************** default filter name start*******************/

    /**
     * 分片字符串连接字符
     */
    public static final String SHARDING_TABLE_CONNECT_CHAR = "_";


    /*******************
     * sharding thread config start
     *********************/
    public static final String SHARDING_CORE_THREAD = "sharding_core_thread";
    public static final String SHARDING_MAX_THREAD = "sharding_max_thread";
    public static final String SHARDING_QUEUE_LENGTH = "sharding_queue_length";

    public static final int DEFAULT_SHARDING_CORE_THREAD_NUM = 5;
    public static final int DEFAULT_SHARDING_MAX_THREAD_NUM = 30;
    public static final int DEFALUT_SHARDING_QUEUE_LENGTH_NUM = 100;
    /******************* sharding thread config end*********************/
}
