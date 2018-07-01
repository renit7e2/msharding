package com.cn.msharding.core.strategy;

import java.util.List;

/**
 * <pre>Created by Kaizen Xue on 2018/4/26.</pre>
 */
public class AllTableChooseStrategy implements TableChooseStrategy<List<String>> {
    @Override
    public List<String> choose(List<String> availableTables) {
        return availableTables;
    }
}

