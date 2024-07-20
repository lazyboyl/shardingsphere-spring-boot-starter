package com.lazyboyl.shardingsphere.springboot.starter.util;

import com.lazyboyl.shardingsphere.springboot.starter.constant.ShardingSphereConstant;

/**
 * 将spring配置转换为shardingsphere可以识别的yml配置
 *
 * @author linzf
 */
public class ShardingsphereUtil {

    /**
     * 获取shardingsphere的配置
     *
     * @param cfg
     * @return
     */
    public static String getShardingConfig(String cfg) {
        return parseShardingConfig(parseYamlConfig(cfg));
    }

    /**
     * 组装shardingsphere的配置
     *
     * @param shardingConfig
     * @return
     */
    private static String parseShardingConfig(String shardingConfig) {
        StringBuilder sb = new StringBuilder();
        Integer subLength = getSubSpaceLength(shardingConfig);
        String[] shardingConfigs = shardingConfig.split("\n");
        for (String s : shardingConfigs) {
            if (s.length() < subLength) {
                continue;
            }
            if (isInclude(s, ShardingSphereConstant.SHARDINGSPHERE_RULES_SHARDING)) {
                sb.append(subStr(s.replaceAll(ShardingSphereConstant.SHARDINGSPHERE_RULES_SHARDING, "- !SHARDING"), subLength)).append("\n");
            } else if (isInclude(s, ShardingSphereConstant.SHARDINGSPHERE_RULES_MASK)) {
                sb.append(subStr(s.replaceAll(ShardingSphereConstant.SHARDINGSPHERE_RULES_MASK, "- !MASK"), subLength)).append("\n");
            } else if (isInclude(s, ShardingSphereConstant.SHARDINGSPHERE_RULES_ENCRYPT)) {
                sb.append(subStr(s.replaceAll(ShardingSphereConstant.SHARDINGSPHERE_RULES_ENCRYPT, "- !ENCRYPT"), subLength)).append("\n");
            } else if (isInclude(s, ShardingSphereConstant.SHARDINGSPHERE_RULES_SINGLE)) {
                sb.append(subStr(s.replaceAll(ShardingSphereConstant.SHARDINGSPHERE_RULES_SINGLE, "- !SINGLE"), subLength)).append("\n");
            } else if (isInclude(s, ShardingSphereConstant.SHARDINGSPHERE_RULES_READWRITE_SPLITTING)) {
                sb.append(subStr(s.replaceAll(ShardingSphereConstant.SHARDINGSPHERE_RULES_READWRITE_SPLITTING, "- !READWRITE_SPLITTING"), subLength)).append("\n");
            } else if (isInclude(s, ShardingSphereConstant.SHARDINGSPHERE_RULES_BROADCAST)) {
                sb.append(subStr(s.replaceAll(ShardingSphereConstant.SHARDINGSPHERE_RULES_BROADCAST, "- !BROADCAST"), subLength)).append("\n");
            } else {
                sb.append(subStr(s, subLength)).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 解析加载下来的yml的配置
     *
     * @param cfg
     * @return
     */
    private static String parseYamlConfig(String cfg) {
        StringBuilder sb = new StringBuilder();
        // 根据换行符来拆分配置文件
        String[] cfgs = cfg.split("\n");
        boolean firstCheck = false;
        boolean secondCheck = false;
        Integer subLength = 0;
        for (String s : cfgs) {
            if (firstCheck) {
                if (s.replaceAll("\n", "").indexOf(ShardingSphereConstant.SHARDINGSPHERE_SECOND_PREFIX) != -1) {
                    secondCheck = true;
                    subLength = s.split(ShardingSphereConstant.SHARDINGSPHERE_SECOND_PREFIX)[0].length();
                    continue;
                }
            }
            if (s.replaceAll("\n", "").equals(ShardingSphereConstant.SHARDINGSPHERE_FIRST_PREFIX)) {
                firstCheck = true;
                continue;
            }
            if (secondCheck) {
                if (s.trim().equals("")) {
                    continue;
                }
                if (s.trim().startsWith("#")) {
                    continue;
                }
                // 判断配置读取完成的位置
                if (s.matches("^\\s{" + (subLength) + "}[a-zA-Z0-9!@#$%^&*()\\-_=+{};:,<.>]+$")) {
                    break;
                }
                sb.append(s).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 获取需要截断的空格长度
     *
     * @param shardingConfig
     * @return
     */
    private static Integer getSubSpaceLength(String shardingConfig) {
        Integer spaceLength = 0;
        String[] shardingConfigs = shardingConfig.split("\n");
        for (String s : shardingConfigs) {
            if (s.replaceAll("\n", "").indexOf(ShardingSphereConstant.SHARDINGSPHERE_SPACE_PRESIX) != -1) {
                return s.split(ShardingSphereConstant.SHARDINGSPHERE_SPACE_PRESIX)[0].length();
            }
        }
        return spaceLength;
    }

    /**
     * 实现字符串的截取
     *
     * @param s
     * @param start
     * @return
     */
    private static String subStr(String s, Integer start) {
        if (s.length() > start) {
            String subStr = s.substring(start);
            if (s.trim().startsWith("#") && !subStr.trim().startsWith("#")) {
                return s;
            }
            return subStr;
        }
        return s;
    }

    /**
     * 判断当前字符串是否包含key中的关键字
     *
     * @param str
     * @param key
     * @return
     */
    private static Boolean isInclude(String str, String key) {
        return str.indexOf(key) != -1;
    }

}
