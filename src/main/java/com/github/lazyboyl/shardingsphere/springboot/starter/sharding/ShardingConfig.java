package com.github.lazyboyl.shardingsphere.springboot.starter.sharding;

import com.github.lazyboyl.shardingsphere.springboot.starter.constant.ShardingSphereConstant;
import com.github.lazyboyl.shardingsphere.springboot.starter.util.ShardingsphereUtil;
import com.github.lazyboyl.shardingsphere.springboot.starter.util.StrUtil;
import com.github.lazyboyl.shardingsphere.springboot.starter.util.PropertiesParser;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.cloud.bootstrap.config.BootstrapPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 读取
 *
 * @author linzf
 */
@Component
public class ShardingConfig {

    private Logger logger = LoggerFactory.getLogger(ShardingConfig.class);

    @Resource
    private ConfigurableEnvironment environment;

    @Bean
    public DataSource dataSource() {
        // 获取所有的配置文件，例如appliactiom.yml,boostrap.yml,配置中心拉取的配置等
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> propertiesMap = new HashMap<>();
        for (org.springframework.core.env.PropertySource<?> propertySource : propertySources) {
            // nacos上拉取下来的配置
            if (propertySource instanceof BootstrapPropertySource) {
                BootstrapPropertySource bootstrapPropertySource = (BootstrapPropertySource) propertySource;
                Object obj = bootstrapPropertySource.getSource();
                if (obj instanceof LinkedHashMap) {
                    for (Map.Entry<?, ?> entry : ((LinkedHashMap<?, ?>) obj).entrySet()) {
                        String key = StrUtil.getObjStr(entry.getKey());
                        if (key.startsWith(ShardingSphereConstant.SHARDINGSPHERE_STARTS_KEY)) {
                            propertiesMap.put(key, entry.getValue());
                        }
                    }
                }
            }
            // 从yaml上拉取下来的配置
            if (propertySource instanceof OriginTrackedMapPropertySource) {
                OriginTrackedMapPropertySource originTrackedMapPropertySource = (OriginTrackedMapPropertySource) propertySource;
                for (Map.Entry<String, Object> entry : originTrackedMapPropertySource.getSource().entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith(ShardingSphereConstant.SHARDINGSPHERE_STARTS_KEY)) {
                        propertiesMap.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        // 组装properties配置
        StringBuilder sb = new StringBuilder();
        String key = "";
        for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
            key = entry.getKey();
            if (key.indexOf(".props.special-chars") != -1 || key.indexOf(".props.replace-char") != -1) {
                sb.append(entry.getKey()).append("=").append("'").append(entry.getValue()).append("'").append("\n");
            } else {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }
        }
        if (sb.toString().equals("")) {
            throw new RuntimeException("请配置shardingsphere");
        }
        // 将properties转换为yml
        String configYml = PropertiesParser.castToYaml(sb.toString());
        logger.debug("配置为：\n{}", configYml);
        String shardingConfig = ShardingsphereUtil.getShardingConfig(configYml);
        logger.debug("转换以后的配置为：\n{}", shardingConfig);
        DataSource dataSource = null;
        try {
            dataSource = YamlShardingSphereDataSourceFactory.createDataSource(shardingConfig.getBytes(StandardCharsets.UTF_8));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        return dataSource;
    }

}
