package com.cn.msharding.core.returnresult.merge;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/7/23.</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReturnMergeDispatcher {

    private static class DisPatcherHolder {
        private static ReturnMergeDispatcher INSTANCE = new ReturnMergeDispatcher();
    }

    public static ReturnMergeDispatcher getInstance() {
        return DisPatcherHolder.INSTANCE;
    }

    /**
     * 归并结果集
     *
     * @param resultSets
     * @param returnClazz
     * @param <T>
     * @return
     */
    public <T> Object merge(List<Object> resultSets, Class<T> returnClazz) {
        if (CollectionUtils.isEmpty(resultSets)) {
            return new ArrayList<>();
        }
        if (returnClazz.isPrimitive()) {
            return new PrimitiveMerger().merge(resultSets, returnClazz);
        }
        if (returnClazz.isAssignableFrom(Collection.class)) {
            return new CollectionMerger().merge(resultSets, (Class<Collection>) returnClazz);
        }
        return new SingleResultMerger().merge(resultSets, returnClazz);
    }
}
