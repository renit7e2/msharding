package com.cn.msharding.core.returnresult.type;


import com.cn.msharding.common.util.number.NumberUtil;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/5/3.</pre>
 */
public class NumberReturnType implements IShardingReturnType<Number> {
    @Override
    public Number convert(List<Number> numbers) {
        return NumberUtil.add(numbers);
    }
}
