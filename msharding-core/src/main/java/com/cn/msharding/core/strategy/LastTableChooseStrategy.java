package com.cn.msharding.core.strategy;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/4/26.</pre>
 */
public class LastTableChooseStrategy implements TableChooseStrategy<String> {
    @Override
    public String choose(List<String> availableTables) {
        return availableTables.get(availableTables.size() - 1);
    }
}
