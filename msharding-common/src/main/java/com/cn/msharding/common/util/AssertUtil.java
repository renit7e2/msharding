package com.cn.msharding.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * 断言工具
 * <pre>Created by Kaizen Xue</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertUtil {

    /**
     * 不能等于0
     *
     * @param message error message
     * @param params  compared num
     */
    public static <T extends Number> void notEqual0(String message, T... params) {
        BigDecimal num;
        for (Number tmp : params) {
            num = BigDecimal.valueOf(tmp.doubleValue());
            if (num.compareTo(BigDecimal.ZERO) == 0) {
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "this expression must be true");
    }

    public static void notEmpty(Object[] array) {
        notEmpty(array, "array can not be empty");
    }

    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map map) {
        notEmpty(map, "map can not be not empty");
    }

    public static void notEmpty(Map map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection collection) {
        notEmpty(collection, "collection can not be empty");
    }

    public static void notEmpty(Collection collection, String message) {
        if (null == collection || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notBlank(String object) {
        notNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notBlank(String object, String message) {
        if (object == null || object.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * number不能小于0
     *
     * @param number
     * @param message
     */
    public static <T extends Number> void notLessThan0(T number, String message) {
        notLessThan(number, 0, message);
    }

    public static <T extends Number> void notLessThan0(T number) {
        notLessThan(number, 0, "can not less than 0");
    }

    /**
     * number不能小于等于0
     *
     * @param number
     * @param message
     */
    public static <T extends Number> void notLessThanOrEql0(T number, String message) {
        notLessThanOrEql(number, 0, message);
    }

    public static <T extends Number> void notLessThanOrEql0(T number) {
        notLessThanOrEql(number, 0, "can not less than or equals 0");
    }

    /**
     * number不能小于num
     *
     * @param number
     * @param num
     * @param message
     */
    private static <T extends Number> void notLessThan(T number, long num, String message) {
        notNull(number, message);
        if (number instanceof BigDecimal) {
            if (((BigDecimal) number).compareTo(BigDecimal.valueOf(num)) < 0) {
                throw new IllegalArgumentException(message);
            }
            return;
        }
        if (number instanceof Long) {
            Long number1 = (Long) number;
            if (number1 < num) {
                throw new IllegalArgumentException(message);
            }
            return;
        }
        Integer number1 = (Integer) number;
        if (number1 < num) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * number不能小于等于num
     *
     * @param number
     * @param num
     * @param message
     */
    private static <T extends Number> void notLessThanOrEql(T number, long num, String message) {
        AssertUtil.notNull(number, message);
        if (number instanceof BigDecimal) {
            if (((BigDecimal) number).compareTo(BigDecimal.valueOf(num)) <= 0) {
                throw new IllegalArgumentException(message);
            }
            return;
        }
        if (number instanceof Long) {
            Long number1 = (Long) number;
            if (number1 <= num) {
                throw new IllegalArgumentException(message);
            }
            return;
        }
        Integer number1 = (Integer) number;
        if (number1 <= num) {
            throw new IllegalArgumentException(message);
        }
    }
}
