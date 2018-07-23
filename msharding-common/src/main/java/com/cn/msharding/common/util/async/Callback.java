package com.cn.msharding.common.util.async;

/**
 * <pre>Created by Kaizen Xue on 2018/7/23.</pre>
 */
public interface Callback<T, R> {
    /**
     * callback
     *
     * @param t param
     * @return result
     */
    R callBack(T t);
}
