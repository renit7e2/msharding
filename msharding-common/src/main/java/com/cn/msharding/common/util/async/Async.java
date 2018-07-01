package com.cn.msharding.common.util.async;

/**
 * <pre>Created by Kaizen Xue on 2018/3/30.</pre>
 */
public interface Async<V, T> {
    /**
     * run method
     *
     * @param t thread param
     * @return result
     */
    V run(T t) throws Exception;
}
