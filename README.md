shardingsphere-spring-boot-starter [![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
===================================

### 1、简介
在应用程序中你只需要几行代码就可以快速的构建websocket服务。
### 2、要求
- jdk版本为1.8或1.8+
- shardingsphere版本为5.5.0
- spring boot版本为2.3.12.RELEASE+

### 3、快速开始

#### 3.1、添加依赖

```xml
<dependency>
   <groupId>com.lazyboyl</groupId>
   <artifactId>shardingsphere-spring-boot-starter</artifactId>
   <version>5.5.0.M1</version>
</dependency>
```

#### 3.2、添加注解

在spring boot的启动类上加上`@EnableWebSocketServer`注解。

```java
@SpringBootApplication
@EnableShardingSphereSpringStarter
public class ShardingSpringBootStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingSpringBootStartApplication.class, args);
    }

}
```

### 4、YAML配置

### 4.1、模式配置

```yaml
spring:
  shardingsphere:
    mode (?): # 不配置则默认单机模式
      type: # 运行模式类型。可选配置：Standalone、Cluster
      repository (?): # 持久化仓库配置
```

### 4.2、数据源配置

```yaml
spring:
  shardingsphere:
    dataSources: # 数据源配置，可配置多个 <data-source-name>
      <data_source_name>: # 数据源名称
        dataSourceClassName: # 数据源完整类名
        driverClassName: # 数据库驱动类名，以数据库连接池自身配置为准
        jdbcUrl: # 数据库 URL 连接，以数据库连接池自身配置为准
        username: # 数据库用户名，以数据库连接池自身配置为准
        password: # 数据库密码，以数据库连接池自身配置为准
        # ... 数据库连接池的其它属性
```

### 4.3、规则配置

#### 4.1、数据分片

```yaml
spring:
  shardingsphere:
    rules:
      sharding:
        tables: # 数据分片规则配置
          <logic_table_name> (+): # 逻辑表名称
            actualDataNodes (?): # 由数据源名 + 表名组成（参考 Inline 语法规则）
            databaseStrategy (?): # 分库策略，缺省表示使用默认分库策略，以下的分片策略只能选其一
              standard: # 用于单分片键的标准分片场景
                shardingColumn: # 分片列名称
                shardingAlgorithmName: # 分片算法名称
              complex: # 用于多分片键的复合分片场景
                shardingColumns: # 分片列名称，多个列以逗号分隔
                shardingAlgorithmName: # 分片算法名称
              hint: # Hint 分片策略
                shardingAlgorithmName: # 分片算法名称
              none: # 不分片
            tableStrategy: # 分表策略，同分库策略
            keyGenerateStrategy: # 分布式序列策略
              column: # 自增列名称，缺省表示不使用自增主键生成器
              keyGeneratorName: # 分布式序列算法名称
            auditStrategy: # 分片审计策略
              auditorNames: # 分片审计算法名称
                - <auditor_name>
                - <auditor_name>
              allowHintDisable: true # 是否禁用分片审计hint
        autoTables: # 自动分片表规则配置
          t_order_auto: # 逻辑表名称
            actualDataSources (?): # 数据源名称
            shardingStrategy: # 切分策略
              standard: # 用于单分片键的标准分片场景
                shardingColumn: # 分片列名称
                shardingAlgorithmName: # 自动分片算法名称
        bindingTables (+): # 绑定表规则列表
          - <logic_table_name_1, logic_table_name_2, ...> 
          - <logic_table_name_1, logic_table_name_2, ...> 
        defaultDatabaseStrategy: # 默认数据库分片策略
        defaultTableStrategy: # 默认表分片策略
        defaultKeyGenerateStrategy: # 默认的分布式序列策略
        defaultShardingColumn: # 默认分片列名称
  
        # 分片算法配置
        shardingAlgorithms:
          <sharding_algorithm_name> (+): # 分片算法名称
            type: # 分片算法类型
            props: # 分片算法属性配置
            # ...
  
        # 分布式序列算法配置
        keyGenerators:
          <key_generate_algorithm_name> (+): # 分布式序列算法名称
            type: # 分布式序列算法类型
            props: # 分布式序列算法属性配置
            # ...
        # 分片审计算法配置
        auditors:
          <sharding_audit_algorithm_name> (+): # 分片审计算法名称
            type: # 分片审计算法类型
            props: # 分片审计算法属性配置
            # ...
```

#### 4.2、广播表

```yaml
spring:
  shardingsphere:
    rules:
      broadcast:
        tables: # 广播表规则列表
          - <table_name>
          - <table_name>
```

#### 4.3、读写分离

```yaml
spring:
  shardingsphere:
    rules:
      readwrite_splitting:
        dataSources:
          <data_source_name> (+): # 读写分离逻辑数据源名称，默认使用 Groovy 的行表达式 SPI 实现来解析
            write_data_source_name: # 写库数据源名称，默认使用 Groovy 的行表达式 SPI 实现来解析
            read_data_source_names: # 读库数据源名称，多个从数据源用逗号分隔，默认使用 Groovy 的行表达式 SPI 实现来解析
            transactionalReadQueryStrategy (?): # 事务内读请求的路由策略，可选值：PRIMARY（路由至主库）、FIXED（同一事务内路由至固定数据源）、DYNAMIC（同一事务内路由至非固定数据源）。默认值：DYNAMIC
            loadBalancerName: # 负载均衡算法名称
        # 负载均衡算法配置
        loadBalancers:
          <load_balancer_name> (+): # 负载均衡算法名称
            type: # 负载均衡算法类型
            props: # 负载均衡算法属性配置
            # ...    
```

#### 4.4、分布式事务

```yaml
spring:
  shardingsphere:
    transaction:
      defaultType: # 事务模式，可选值 LOCAL/XA/BASE
      providerType: # 指定模式下的具体实现
```

#### 4.5、数据加密

```yaml
spring:
  shardingsphere:
    rules:
      encrypt:
        tables:
          <table_name> (+): # 加密表名称
            columns:
              <column_name> (+): # 加密列名称
                cipher:
                  name: # 密文列名称
                  encryptorName: # 密文列加密算法名称
                # 查阅源代码以后发现已经没有支持辅助查询和模糊查询了
#                assistedQuery (?):  
#                  name: # 查询辅助列名称
#                  encryptorName:  # 查询辅助列加密算法名称
 #               likeQuery (?):
  #                name: # 模糊查询列名称
   #               encryptorName:  # 模糊查询列加密算法名称
    
        # 加密算法配置
        encryptors:
          <encrypt_algorithm_name> (+): # 加解密算法名称
            type: # 加解密算法类型
            props: # 加解密算法属性配置
              # ...
```

#### 4.6、数据脱敏

```yaml
spring:
  shardingsphere:
    rules:
      mask:
        tables:
          <table_name> (+): # 脱敏表名称
            columns:
              <column_name> (+): # 脱敏列名称
                maskAlgorithm: # 脱敏算法
        # 脱敏算法配置
        maskAlgorithms:
          <mask_algorithm_name> (+): # 脱敏算法名称
            type: # 脱敏算法类型
            props: # 脱敏算法属性配置
            # ...
```

#### 4.7、影子库

```yaml
spring:
  shardingsphere:
    rules:
      shadow:
        dataSources:
          shadowDataSource:
            productionDataSourceName: # 生产数据源名称
            shadowDataSourceName: # 影子数据源名称
        tables:
          <table_name>:
            dataSourceNames: # 影子表关联影子数据源名称列表
              - <shadow_data_source>
            shadowAlgorithmNames: # 影子表关联影子算法名称列表
              - <shadow_algorithm_name>
        defaultShadowAlgorithmName: # 默认影子算法名称（选配项）
        shadowAlgorithms:
          <shadow_algorithm_name> (+): # 影子算法名称
            type: # 影子算法类型
            props: # 影子算法属性配置
```

#### 4.8、SQL解析

```yaml
spring:
  shardingsphere:
    sqlParser:
      sqlStatementCache: # SQL 语句本地缓存配置项
        initialCapacity: # 本地缓存初始容量
        maximumSize: # 本地缓存最大容量
      parseTreeCache: # 解析树本地缓存配置项
        initialCapacity: # 本地缓存初始容量
        maximumSize: # 本地缓存最大容量
```

#### 4.9、SQL翻译

```yaml
spring:
  shardingsphere:
    sqlTranslator:
      type: # SQL 翻译器类型
      useOriginalSQLWhenTranslatingFailed: # SQL 翻译失败是否使用原始 SQL 继续执行
```

#### 4.10、单表

```yaml
spring:
  shardingsphere:
    rules:
      single:
        tables:
          - table_name_1
          - table_name_2
```

