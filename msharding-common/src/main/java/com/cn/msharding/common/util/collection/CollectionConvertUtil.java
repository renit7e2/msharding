package com.cn.msharding.common.util.collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 集合转换工具类
 * <pre>Created by Kaizen Xue.</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionConvertUtil {
    public static <T> List<T> collectionToList(Collection<T> collection) {
        List<T> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(collection)) {
            result.addAll(collection);
        }
        return result;
    }

    public static <T> T[] collectionToArray(Collection<T> collection) {
        return (T[]) collectionToList(collection).toArray();
    }

    public static <T> List<T> arrayToList(T[] array) {
        return Arrays.asList(array);
    }
}
