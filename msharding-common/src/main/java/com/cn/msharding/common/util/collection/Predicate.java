package com.cn.msharding.common.util.collection;

/**
 * <pre>Created by Kaizen Xue</pre>
 */
public interface Predicate<T> {
    /**
     * @param t 校验对象
     * @return 是否成功
     * @see org.apache.commons.collections.Predicate
     */
    boolean evaluate(T t);
}
