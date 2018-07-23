package com.cn.msharding.core.returnresult.merge;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/7/23.</pre>
 */
public class SingleResultMerger implements IMerger {
    @Override
    public Object merge(List resultSets, Class returnClazz) {
        for (Object object : resultSets) {
            if (object instanceof List) {
                if (CollectionUtils.isNotEmpty((List) object)) {
                    return object;
                }
            }
        }
        return null;
    }
}
