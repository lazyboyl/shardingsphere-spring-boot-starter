package com.github.lazyboyl.shardingsphere.springboot.starter.constant;

/**
 * shardingsphere的静态常量类
 *
 * @author linzf
 */
public class ShardingSphereConstant {

    /**
     * 区分是否起始的关键字
     */
    public static final String SHARDINGSPHERE_STARTS_KEY = "spring.shardingsphere";

    /**
     * 区分shardingsphere配置的前缀
     */
    public static final String SHARDINGSPHERE_FIRST_PREFIX = "spring:";

    /**
     * 区分shardingsphere配置的前缀
     */
    public static final String SHARDINGSPHERE_SECOND_PREFIX = "shardingsphere:";


    /**
     * 用于截取空字符串长度的标识
     */
    public static final String SHARDINGSPHERE_SPACE_PRESIX = "dataSources:";

    /**
     * 把sharding:替换为- !SHARDING
     */
    public static final String SHARDINGSPHERE_RULES_SHARDING = "sharding:";

    /**
     * 把mask:替换为- !MASK
     */
    public static final String SHARDINGSPHERE_RULES_MASK = "mask:";

    /**
     * 把encrypt:替换为- !ENCRYPT
     */
    public static final String SHARDINGSPHERE_RULES_ENCRYPT = "encrypt:";

    /**
     * 把single:替换为- !SINGLE
     */
    public static final String SHARDINGSPHERE_RULES_SINGLE = "single:";

    /**
     * 把shadow:替换为- !SHADOW
     */
    public static final String SHARDINGSPHERE_RULES_SHADOW = "shadow:";


    /**
     * 把readwrite_splitting:替换为- !READWRITE_SPLITTING
     */
    public static final String SHARDINGSPHERE_RULES_READWRITE_SPLITTING = "readwrite_splitting:";

    /**
     * 把broadcast:替换为- !BROADCAST
     */
    public static final String SHARDINGSPHERE_RULES_BROADCAST = "broadcast:";


    /**
     * 顶层节点映射
     */
    public static final String[] SHARDING_TOP_BABEL = new String[]{
            "databaseName", "dataSources", "rules", "mode", "authority",
            "sqlParser", "transaction", "globalClock", "sqlFederation",
            "sqlTranslator", "traffic", "logging", "props"
    };


}
