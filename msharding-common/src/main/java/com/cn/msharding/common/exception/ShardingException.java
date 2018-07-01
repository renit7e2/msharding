package com.cn.msharding.common.exception;

/**
 * sharding exception
 * <pre>Created by Kaizen Xue</pre>
 */
public class ShardingException extends RuntimeException {
    public ShardingException() {
        super();
    }

    public ShardingException(String message) {
        super(message);
    }

    public ShardingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShardingException(Throwable cause) {
        super(cause);
    }
}
