package com.cn.msharding.common.util.async;

import com.cn.msharding.common.exception.AsyncException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * <pre>Created by Kaizen Xue on 2018/7/23.</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsyncResultUtil {

    public static <T> T getResult(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new AsyncException("get async result exception:" + e.getMessage(), e);
        } catch (ExecutionException e) {
            throw new AsyncException("get async result exception:" + e.getMessage(), e);
        }
    }

    public static <T> List<T> getResultList(List<Future<T>> futures) {
        List<T> result = new ArrayList<T>();
        if (!CollectionUtils.isEmpty(futures)) {
            for (Future<T> future : futures) {
                T _t = getResult(future);
                if (_t != null) {
                    result.add(_t);
                }
            }
        }
        return result;
    }
}
