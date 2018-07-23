package com.cn.msharding.common.exception;

/**
 * <pre>Created by Kaizen Xue on 2018/7/23.</pre>
 */
public class AsyncException extends RuntimeException {
    public AsyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
