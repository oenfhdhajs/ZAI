package cn.z.zai.config.database;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "dynamic")
public class DynamicDataSourceProperties {
    private Map<String, HikariDataSource> datasource = new LinkedHashMap<>();

    public Map<String, HikariDataSource> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, HikariDataSource> datasource) {
        this.datasource = datasource;
    }
}
