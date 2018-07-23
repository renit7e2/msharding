package com.cn.msharding.common.util.async;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.*;

/**
 * 线程工具类
 * <pre>Created by Kaizen Xue</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadPoolUtil {
    //核心线程数
    private static final int CORE_THREAD_NUM = 3;
    //最大线程数
    private static final int MAX_THREAD_NUM = 10;
    //单位:秒
    private static final long MAX_WAIT_TIME = 30L;
    //默认队列长度
    private static final int QUEUE_LENGTH = 100;
    //默认线程池
    private static final ExecutorService executorService = new ThreadPoolExecutor(CORE_THREAD_NUM, MAX_THREAD_NUM, MAX_WAIT_TIME,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(QUEUE_LENGTH), new ThreadPoolExecutor.AbortPolicy());


    /**
     * {@link #asyncCall(ExecutorService, Object, Async)}
     */
    public static <V, T> Future<V> asyncCall(final T t, final Async<V, T> asyncThread) {
        return asyncCall(executorService, t, asyncThread);
    }

    /**
     * 异步调用
     *
     * @param executor    线程池
     * @param t           请求参数
     * @param asyncThread 线程执行接口
     * @param <V>         返回结果泛型
     * @param <T>         请求参数泛型
     * @return
     */
    public static <V, T> Future<V> asyncCall(ExecutorService executor, final T t, final Async<V, T> asyncThread) {
        Callable<V> callable = new Callable<V>() {
            @Override
            public V call() throws Exception {
                return asyncThread.run(t);
            }
        };
        return executor.submit(callable);
    }

    /**
     * {@link #asyncCall(ExecutorService, Object, Async, Callback)}
     */
    public static <V1, T, V0> V1 asyncCall(final T t, final Async<V0, T> asyncThread, Callback<V0, V1> callback) {
        V0 result = AsyncResultUtil.getResult(asyncCall(t, asyncThread));
        if (callback != null) {
            return callback.callBack(result);
        }
        return null;
    }

    /**
     * 异步调用&&回调
     *
     * @param executor    线程池
     * @param t           请求参数
     * @param asyncThread 线程执行接口
     * @param callback    回调函数
     * @param <V1>        回调返回泛型
     * @param <T>         请求参数泛型
     * @param <V0>        线程返回结果泛型
     * @return
     */
    public static <V1, T, V0> V1 asyncCall(ExecutorService executor, final T t, final Async<V0, T> asyncThread, Callback<V0, V1> callback) {
        V0 result = AsyncResultUtil.getResult(asyncCall(executor, t, asyncThread));
        if (callback != null) {
            return callback.callBack(result);
        }
        return null;
    }
}
