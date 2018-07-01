package com.cn.msharding.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>Created by Kaizen Xue on 2018/4/4.</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class BlockUtil {

    public static void waitTime(long millis) {
        sleep(millis);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("thread interrupt", e);
            Thread.currentThread().interrupt();
        }
    }
}
