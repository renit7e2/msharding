package com.cn.msharding.core.returnresult.merge;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/7/23.</pre>
 */
public interface IMerger<T> {
    /**
     * merge result
     *
     * @param resultSets
     * @param returnClazz
     * @return
     */
    T merge(List<Object> resultSets, Class<T> returnClazz);
}
