package com.cn.msharding.core.returntype;


import com.cn.msharding.common.exception.ShardingRunException;
import com.cn.msharding.common.util.AssertUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>Created by Kaizen Xue.</pre>
 */
public class ReturnTypeManager {
    private static ReturnTypeManager INSTANCE = new ReturnTypeManager();

    private volatile Map<Class<?>, IShardingReturnType<?>> returnTypeMap = new HashMap<>();

    private ReturnTypeManager() {
        registerReturnType();
    }

    public static ReturnTypeManager getInstance() {
        return INSTANCE;
    }

    private void registerReturnType() {
        returnTypeMap.put(Void.class, new VoidReturnType());
        returnTypeMap.put(Number.class, new NumberReturnType());
        returnTypeMap.put(List.class, new ListReturnType());
        returnTypeMap.put(Set.class, new SetReturnType());
    }

    public IShardingReturnType getReturnType(Class clazz) {
        AssertUtil.notNull(clazz, "class can not be null");
        if (clazz.equals(Void.class) || clazz.equals(void.class)) {
            return returnTypeMap.get(Void.class);
        }
        if (clazz.isPrimitive()) {
            return returnTypeMap.get(Number.class);
        }
        IShardingReturnType<?> returnType = returnTypeMap.get(clazz);
        if (returnType == null) {
            for (Class returnClazz : returnTypeMap.keySet()) {
                if (clazz.isAssignableFrom(returnClazz)) {
                    returnType = returnTypeMap.get(returnClazz);
                    break;
                }
            }
        }
        if (returnType == null) {
            throw new ShardingRunException("unsupported return type,clazz=" + clazz);
        }
        return returnType;
    }
}
