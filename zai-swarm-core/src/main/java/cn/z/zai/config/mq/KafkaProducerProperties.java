package cn.z.zai.config.mq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducerProperties {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Integer retries;

    private Integer maxBlockMs;

    private Integer batchSize;

    private Integer lingerMs;

    private Integer bufferMemory;

    private Integer maxRequestSize;

    private Class<?> keySerializer;

    private Class<?> valueSerializer;

    private String partitionerClass;

    private String acks;
}
