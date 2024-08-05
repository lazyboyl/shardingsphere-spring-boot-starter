package com.github.lazyboyl.shardingsphere.springboot.starter.scanner;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 配置是否开启nacos的starter
 *
 * @author linzf
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({ShardingsphereRegister.class})
public @interface EnableShardingSphereSpringStarter {

    /**
     * 功能描述： 需要进行spring扫描的目录
     *
     * @return String[]
     */
    String[] basePackages() default {"com.github.lazyboyl.shardingsphere.springboot.starter"};

}
