package com.cn.msharding.core.strategy;

import java.util.List;
import java.util.Random;

/**
 * <pre>Created by Kaizen Xue on 2018/4/26.</pre>
 */
public class RandomTableChooseStrategy implements TableChooseStrategy<String> {
    @Override
    public String choose(List<String> availableTables) {
        int num = new Random(System.nanoTime()).nextInt(availableTables.size());
        return availableTables.get(num);
    }
}
