package com.cn.msharding.core.returnresult.type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>Created by Kaizen Xue on 2018/5/4.</pre>
 */
public class SetReturnType implements IShardingReturnType<Set> {
    @Override
    public Set convert(List<Set> sets) {
        Set result = new HashSet();
        for (Set<Set> list : sets) {
            result.addAll(list);
        }
        return result;
    }
}
