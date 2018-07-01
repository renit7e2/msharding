package com.cn.msharding.core.returntype;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/5/3.</pre>
 */
public class VoidReturnType implements IShardingReturnType<Void> {
    @Override
    public Void convert(List<Void> voids) {
        return null;
    }
}
