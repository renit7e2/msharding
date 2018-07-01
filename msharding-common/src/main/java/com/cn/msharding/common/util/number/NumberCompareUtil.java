package com.cn.msharding.common.util.number;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 数字比较工具类
 * <pre>Created by Kaizen Xue.</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberCompareUtil {
    /**
     * 是否相等【类型应该相同】
     *
     * @param standard   被比较数
     * @param compareNum 比较数
     * @return is compareNum equal than standard
     */
    public static <T extends Number> boolean isEqual(T standard, T compareNum) {
        if (standard instanceof BigDecimal) {
            return ((BigDecimal) standard).compareTo((BigDecimal) compareNum) == 0;
        }
        return standard.equals(compareNum);
    }

    /**
     * is compareNum less than standard
     *
     * @param standard   被比较数
     * @param compareNum 比较数
     * @return is compareNum less than standard
     */
    public static <T extends Number> boolean isLess(T standard, T compareNum) {
        if (standard instanceof BigDecimal) {
            return ((BigDecimal) standard).compareTo((BigDecimal) compareNum) > 0;
        }
        if (standard instanceof Integer) {
            return ((Integer) standard).compareTo((Integer) compareNum) > 0;
        }
        if (standard instanceof Long) {
            return ((Long) standard).compareTo((Long) compareNum) > 0;
        }
        if (standard instanceof Double) {
            return ((Double) standard).compareTo((Double) compareNum) > 0;
        }
        if (standard instanceof Float) {
            return ((Float) standard).compareTo((Float) compareNum) > 0;
        }
        throw new IllegalArgumentException("not found param type.");
    }

    /**
     * is compareNum greater than standard
     *
     * @param standard   被比较数
     * @param compareNum 比较数
     * @return is compareNum greater than standard
     */
    public static <T extends Number> boolean isGreater(T standard, T compareNum) {
        return isLess(compareNum, standard);
    }

    public static <T extends Number> boolean isGreatOrEqual(T standard, T compareNum) {
        if (isEqual(standard, compareNum)) {
            return true;
        }
        if (isGreater(standard, compareNum)) {
            return true;
        }
        return false;
    }

    public static <T extends Number> boolean isLessOrEqual(T standard, T compareNum) {
        if (isEqual(standard, compareNum)) {
            return true;
        }
        if (isLess(standard, compareNum)) {
            return true;
        }
        return false;
    }

    public static <T extends Number> T min(T standard, T compareNum) {
        if (isLess(standard, compareNum)) {
            return compareNum;
        }
        return standard;
    }
}
