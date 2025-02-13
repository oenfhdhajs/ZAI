package cn.z.zai.config.database;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Maps;
import java.util.Map;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

/**
 *
 *
 * @author gang.shen
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfig {
    @Autowired
    private DynamicDataSourceProperties properties;



    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }
        return dataSource;
    }

    @Bean
    public DynamicDataSource dynamicDataSource(HikariDataSource hikariDataSource) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> sourceMap = getDynamicDataSource();
        dynamicDataSource.setTargetDataSources(sourceMap);
        //def
        dynamicDataSource.setDefaultTargetDataSource(hikariDataSource);
        return dynamicDataSource;
    }

    private Map<Object, Object> getDynamicDataSource(){
        Map<String, HikariDataSource> dataSourcePropertiesMap = properties.getDatasource();
        Map<Object, Object> targetDataSources = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(dataSourcePropertiesMap)) {
            dataSourcePropertiesMap.forEach((k, v) -> {
                HikariDataSource hikariDataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
                hikariDataSource.setJdbcUrl(v.getJdbcUrl());
                hikariDataSource.setDriverClassName(v.getDriverClassName());
                hikariDataSource.setUsername(v.getUsername());
                hikariDataSource.setPassword(v.getPassword());
                targetDataSources.put(k, hikariDataSource);
            });
        }
        return targetDataSources;
    }

}