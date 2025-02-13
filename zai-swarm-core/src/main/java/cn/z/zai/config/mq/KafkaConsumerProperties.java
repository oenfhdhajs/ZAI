package cn.z.zai.config.mq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.consumer")
public class KafkaConsumerProperties {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Integer autoCommitIntervalMs;

    private String sessionTimeoutMs;

    private String autoOffsetReset;

    private Boolean enableAutoCommit;

    private Class<?> keyDeserializer;

    private Class<?> valueDeserializer;

    private Integer maxPollRecords;

    private String groupId;
}
