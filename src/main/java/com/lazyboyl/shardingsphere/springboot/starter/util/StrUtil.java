package com.lazyboyl.shardingsphere.springboot.starter.util;

import java.util.Map;

/**
 * 字符串工具类
 *
 * @author linzf
 */
public class StrUtil {

    public static final String LF = "\n";

    /**
     * 空字符串
     */
    private static final String EMPTY_STR = "";

    public static boolean isNotEmpty(String str) {
        return (str != null && !EMPTY_STR.equals(str.trim()));
    }

    public static boolean mapIsEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    public static String getObjStr(Object obj) {
        return obj == null ? "" : obj.toString().trim();
    }

}
