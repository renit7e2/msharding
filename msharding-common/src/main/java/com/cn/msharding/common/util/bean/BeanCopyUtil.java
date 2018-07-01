package com.cn.msharding.common.util.bean;

import com.cn.msharding.common.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体拷贝工具类
 * <pre>Created by Kaizen Xue</pre>
 */
@Slf4j
public class BeanCopyUtil {
    private BeanCopyUtil() {
    }

    public static <T> T copy(T t, Class<T> clazz) {
        AssertUtil.notNull(t);
        AssertUtil.notNull(clazz);

        T result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(result, t);
        } catch (InstantiationException e) {
            log.error("newInstance error.", e);
        } catch (IllegalAccessException e) {
            log.error("newInstance error.", e);
        } catch (InvocationTargetException e) {
            log.error("newInstance error.", e);
        }
        return result;
    }

    public static <T> List<T> listCopy(List<T> t, Class<T> clazz) {
        AssertUtil.notEmpty(t);
        AssertUtil.notNull(clazz);

        List<T> result = new ArrayList<>();
        for (T temp : t) {
            result.add(copy(temp, clazz));
        }
        return result;
    }
}
