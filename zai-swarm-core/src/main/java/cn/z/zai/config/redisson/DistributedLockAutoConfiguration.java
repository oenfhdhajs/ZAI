package cn.z.zai.config.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 */
@Slf4j
@Component
public class DistributedLockAutoConfiguration {

    @Autowired
    private IRedissonProperties properties;
    @Bean
    public RedissonClient redissonDistributedLocker() {
        // redisson version 3.5
        List<String> nodeList = new ArrayList();
        for (String node : properties.getCluster().getNodes()) {
            nodeList.add("redis://" + node);
        }
        Config config = new Config();
        config.useClusterServers()
                .setTimeout(5000)
                .setScanInterval(2000)
                .setRetryAttempts(3)
                .setRetryInterval(1500)
                .addNodeAddress(nodeList.toArray(new String[0]))
                .setPassword(properties.getPassword());
        RedissonClient redisson = Redisson.create(config);
        log.info("redisson-lock-spring-boot-start");
        return redisson;
    }
}
