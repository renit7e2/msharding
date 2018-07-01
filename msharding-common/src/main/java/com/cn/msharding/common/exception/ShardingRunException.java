package com.cn.msharding.common.exception;

/**
 * 分片运行时异常
 * <pre>Created by Kaizen Xue</pre>
 */
public class ShardingRunException extends ShardingException {
    public ShardingRunException() {
        super();
    }

    public ShardingRunException(String message) {
        super(message);
    }

    public ShardingRunException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShardingRunException(Throwable cause) {
        super(cause);
    }
}
