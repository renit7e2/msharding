package com.cn.msharding.common.util.number;

import com.cn.msharding.common.util.AssertUtil;
import com.cn.msharding.common.util.collection.CollectionConvertUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by kaizen on 2018/6/30.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtil {
    public static <T extends Number> T add(List<T> numbers) {
        if (numbers.get(0) instanceof BigDecimal) {
            BigDecimal result = BigDecimal.ZERO;
            for (T t : numbers) {
                result = result.add((BigDecimal) t);
            }
            return (T) result;
        }
        if (numbers.get(0) instanceof Integer) {
            Integer result = 0;
            for (T t : numbers) {
                result = result + (Integer) t;
            }
            return (T) result;
        }
        if (numbers.get(0) instanceof Long) {
            Long result = 0L;
            for (T t : numbers) {
                result = result + (Long) t;
            }
            return (T) result;
        }
        if (numbers.get(0) instanceof Float) {
            Float result = 0.0F;
            for (T t : numbers) {
                result = result + (Float) t;
            }
            return (T) result;
        }
        if (numbers.get(0) instanceof Double) {
            Double result = 0.0;
            for (T t : numbers) {
                result = result + (Double) t;
            }
            return (T) result;
        }
        throw new UnsupportedOperationException("not support number type");
    }

    public static <T extends Number> T add(T... numbers) {
        AssertUtil.notEmpty(numbers, "array can not be empty");
        if (numbers.length == 1) {
            return numbers[0];
        }
        return add(CollectionConvertUtil.arrayToList(numbers));
    }
}
