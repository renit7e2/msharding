package com.cn.msharding.core.returnresult.merge;

import com.cn.msharding.core.returnresult.type.ReturnTypeManager;

import java.util.Collection;
import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/7/23.</pre>
 */
public class CollectionMerger implements IMerger<Collection> {
    @Override
    public Collection merge(List<Object> resultSets, Class<Collection> returnClazz) {
        return (Collection) ReturnTypeManager.getInstance().getReturnType(returnClazz).convert(resultSets);
    }
}
