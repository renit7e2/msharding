package com.cn.msharding.common.exception;

/**
 * 分片解析异常
 * <pre>Created by Kaizen Xue</pre>
 */
public class ShardingResolveException extends ShardingException {
    public ShardingResolveException() {
        super();
    }

    public ShardingResolveException(String message) {
        super(message);
    }

    public ShardingResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShardingResolveException(Throwable cause) {
        super(cause);
    }
}
