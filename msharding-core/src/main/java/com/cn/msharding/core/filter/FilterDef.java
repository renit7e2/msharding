package com.cn.msharding.core.filter;

import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>Created by Kaizen Xue</pre>
 */
@Getter
public class FilterDef implements Serializable {
    /**
     * filer name to display
     */
    private String displayName;

    /**
     * filter class
     */
    private Class<? extends Filter> filterClass;

    /**
     * initialized param
     */
    private Map<String, String> initParams = new HashMap<>();

    public FilterDef displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public FilterDef filterClass(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    public void addParam(String name, String value) {
        initParams.put(name, value);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
