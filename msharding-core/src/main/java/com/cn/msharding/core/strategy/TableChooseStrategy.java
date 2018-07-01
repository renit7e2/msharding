package com.cn.msharding.core.strategy;

import java.util.List;

/**
 * choose one or some tables from available tables
 * <pre>Created by Kaizen Xue on 2018/4/26.</pre>
 */
public interface TableChooseStrategy<R> {
    /**
     * choose table
     *
     * @param availableTables
     * @return the table or tables chosen
     */
    R choose(List<String> availableTables);
}
