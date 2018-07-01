package com.cn.msharding.common.util.collection;

/**
 * <pre>Created by Kaizen Xue</pre>
 */
public interface Property<S, D> {
    /**
     * 获取属性
     * 获取属性
     *
     * @param s 源对象
     * @return 提取结果
     */
    D gain(S s);
}
