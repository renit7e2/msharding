package com.cn.msharding.core.returnresult.merge;

import com.cn.msharding.core.returnresult.type.ReturnTypeManager;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/7/23.</pre>
 */
public class PrimitiveMerger implements IMerger {
    @Override
    public Object merge(List resultSets, Class returnClazz) {
        return ReturnTypeManager.getInstance().getReturnType(returnClazz).convert(resultSets);
    }
}
