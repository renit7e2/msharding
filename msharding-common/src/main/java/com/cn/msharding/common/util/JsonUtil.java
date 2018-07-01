package com.cn.msharding.common.util;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {
    public static String obj2Str(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T str2Obj(String str, Class<T> clazz) {
        return new Gson().fromJson(str, clazz);
    }

}
