package com.cn.msharding.common.util.bean;

import com.cn.msharding.common.util.AssertUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体转换工具类
 * <pre>Created by Kaizen Xue</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class BeanConvertUtil {
    /**
     * 实体转换
     *
     * @param source 被转换实体
     * @param clazz  目标实体class
     * @return 目标实体
     */
    public static <T> T convert(Object source, Class<T> clazz) {
        AssertUtil.notNull(source);
        T result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(result, source);
        } catch (Exception e) {
            log.error("newInstance error.", e);
        }
        return result;
    }

    /**
     * @see #convert(Object, Class)
     */
    public static <T> List<T> convertList(List sources, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(sources)) {
            return result;
        }
        for (Object source : sources) {
            result.add(convert(source, clazz));
        }
        return result;
    }
}
