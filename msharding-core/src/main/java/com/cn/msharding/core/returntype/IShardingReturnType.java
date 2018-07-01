package com.cn.msharding.core.returntype;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/5/3.</pre>
 */
public interface IShardingReturnType<T> {
    /**
     * convert the return list to one
     *
     * @param ts
     * @return
     */
    T convert(List<T> ts);
}
