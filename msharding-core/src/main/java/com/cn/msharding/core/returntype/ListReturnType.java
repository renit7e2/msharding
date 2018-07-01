package com.cn.msharding.core.returntype;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/5/3.</pre>
 */
public class ListReturnType implements IShardingReturnType<List> {
    @Override
    public List convert(List<List> lists) {
        List result = new ArrayList();
        for (List<List> list : lists) {
            result.addAll(list);
        }
        return result;
    }
}
