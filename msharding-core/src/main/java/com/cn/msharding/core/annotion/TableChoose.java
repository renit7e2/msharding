package com.cn.msharding.core.annotion;


import com.cn.msharding.core.strategy.AllTableChooseStrategy;
import com.cn.msharding.core.strategy.TableChooseStrategy;

import java.lang.annotation.*;

/**
 * 选表策略
 * <pre>Created by Kaizen Xue</pre>
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableChoose {
    Class<? extends TableChooseStrategy> value() default AllTableChooseStrategy.class;
}
