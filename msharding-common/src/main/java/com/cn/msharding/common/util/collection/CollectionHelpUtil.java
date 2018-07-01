package com.cn.msharding.common.util.collection;

import com.cn.msharding.common.util.AssertUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * 集合工具类
 * <pre>Created by Kaizen Xue</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionHelpUtil {

    /**
     * 集合过滤
     *
     * @param collection 集合
     * @param predicate  过滤条件(执行结果为false的过滤掉)
     * @return 过滤的数目
     */
    public static <T> int filter(Collection<T> collection, Predicate<T> predicate) {
        int result = 0;
        if (collection != null && predicate != null) {
            for (Iterator<T> it = collection.iterator(); it.hasNext(); ) {
                if (!predicate.evaluate(it.next())) {
                    it.remove();
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * @see #extract(Object, Property)
     */
    public static <S, D> List<D> extract(List<S> s, Property<S, D> property) {
        List<D> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(s)) {
            return result;
        }
        for (S temp : s) {
            result.add(extract(temp, property));
        }
        return result;
    }


    public static <S, D> Set<D> extractSet(List<S> s, Property<S, D> property) {
        Set<D> result = new HashSet<>();
        if (CollectionUtils.isEmpty(s)) {
            return result;
        }
        for (S temp : s) {
            result.add(extract(temp, property));
        }
        return result;
    }


    /**
     * 提取属性
     *
     * @param s        源对象
     * @param property 获取属性接口
     * @return 目标属性
     */
    public static <S, D> D extract(S s, Property<S, D> property) {
        if (property == null) {
            return null;
        }
        return property.gain(s);
    }


    /**
     * 求两个集合集合减集(big - small)
     *
     * @param big        范围较大的集合
     * @param small      范围较小的集合
     * @param comparator 相减条件
     * @return 减集结果
     */
    public static <T> List<T> subtract(Collection<T> big, Collection<T> small, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(big)) {
            return result;
        }
        result.addAll(big);
        if (CollectionUtils.isEmpty(small)) {
            return result;
        }
        for (T bigTemp : big) {
            for (T smallTep : small) {
                if (comparator.compare(bigTemp, smallTep) == 0) {
                    result.remove(bigTemp);
                }
            }
        }
        return result;
    }

    /**
     * 集合的addAll
     */
    public static <T> void safeAddAll(Collection<T> source, Collection<T>... addCollections) {
        if (source == null) {
            AssertUtil.notNull(source);
        }
        if (ArrayUtils.isNotEmpty(addCollections)) {
            for (Collection<T> collection : addCollections) {
                if (CollectionUtils.isNotEmpty(collection)) {
                    source.addAll(collection);
                }
            }
        }

    }


    public static <T extends Comparable> void sort(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Collections.sort(list);
    }

    public static <T> void sort(List<T> list, Comparator<? super T> comparator) {
        if (CollectionUtils.isEmpty(list) || comparator == null) {
            return;
        }
        Collections.sort(list, comparator);
    }

    public static <T> long getSize(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return 0L;
        }
        return collection.size();
    }

    public static <T> void removeOther(Collection<T> source, Collection<T> contains) {
        if (CollectionUtils.isEmpty(source)) {
            return;
        }
        if (CollectionUtils.isEmpty(contains)) {
            source.clear();
            return;
        }
        Iterator<T> iterator = source.iterator();
        while (iterator.hasNext()) {
            T s = iterator.next();
            if (!contains.contains(s)) {
                iterator.remove();
            }
        }
    }
}
