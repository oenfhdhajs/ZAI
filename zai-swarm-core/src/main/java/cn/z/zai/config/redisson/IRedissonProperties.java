package cn.z.zai.config.redisson;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class IRedissonProperties {

    private Cluster cluster;

    private String password;

    public static class Cluster {

        /**
         * Comma-separated list of "host:port" pairs to bootstrap from. This represents an "initial" list of cluster
         * nodes and is required to have at least one entry.
         */
        private List<String> nodes;

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }
}
