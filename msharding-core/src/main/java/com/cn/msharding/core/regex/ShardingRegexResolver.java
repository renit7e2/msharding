package com.cn.msharding.core.regex;


import com.cn.msharding.common.exception.ShardingResolveException;
import com.cn.msharding.common.util.AssertUtil;
import com.cn.msharding.common.util.number.NumberCompareUtil;
import com.cn.msharding.core.constant.ShardingRegexConstant;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sharing rule regex resolving
 * <pre>Created by Kaizen Xue.</pre>
 */
public class ShardingRegexResolver {

    private static final Pattern rangePattern = Pattern.compile(ShardingRegexConstant.RANGE_REGEX);
    private static final Pattern collectPattern = Pattern.compile(ShardingRegexConstant.CONLLECT_REGEX);
    private static final Pattern numPattern = Pattern.compile(ShardingRegexConstant.NUM_REGEX);


    /**
     * resolving the regex to available sharding table postfix
     *
     * @param tableRegex table regex
     * @return the available table postfix of regex
     */
    public static List<String> resolving(String tableRegex) {
        AssertUtil.notBlank(tableRegex, "sharding tableRegex can not be blank");

        Matcher collectMatcher = collectPattern.matcher(tableRegex);
        String[] collectItems;
        if (collectMatcher.find()) {
            String group = collectMatcher.group();
            collectItems = group.split(ShardingRegexConstant.COLLECT_SPLIT_CHAR);
        } else {
            collectItems = new String[]{tableRegex};
        }

        Set<String> tables = new HashSet<>();
        for (int i = 0; i < collectItems.length; i++) {
            if (!collectItems[i].trim().isEmpty()) {
                tables.addAll(resolvingRangeRegex(collectItems[i]));
            }
        }
        List<String> result = new ArrayList<>(tables);
        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        });
        return result;
    }


    private static Set<String> resolvingRangeRegex(String regex) {
        Set<String> result = new HashSet<>();
        Matcher matcher = rangePattern.matcher(regex);
        boolean isRangeRegex = matcher.find();
        if (numPattern.matcher(regex).find() && !isRangeRegex) {
            result.add(resolvingNumRegex(regex));
            return result;
        }

        if (!isRangeRegex) {
            throw new ShardingResolveException("illegal range regex," + regex);
        }
        String range = matcher.group(1);
        String[] nums = range.split(ShardingRegexConstant.RANGE_CONNECT_CHAR, 2);
        Integer startNum = Integer.valueOf(nums[0]);
        if (startNum <0) {
            throw new ShardingResolveException("range regex startNum can not than 0");
        }
        Integer endNum = Integer.valueOf(nums[1]);
        boolean compare = NumberCompareUtil.isGreater(endNum, startNum);
        if (compare) {
            throw new ShardingResolveException("illegal range regex,start num is bigger than end num." + regex);
        }
        for (int i = startNum; i <= endNum; i++) {
            result.add(String.valueOf(i));
        }
        return result;
    }

    private static String resolvingNumRegex(String regex) {
        Matcher matcher = numPattern.matcher(regex);
        if (!matcher.find()) {
            throw new ShardingResolveException("illegal num regex," + regex);
        }
        String num = matcher.group();
        if (Integer.valueOf(num) <= 0) {
            throw new ShardingResolveException("num regex must be bigger than 0");
        }
        return num;
    }
}
