package cn.z.zai.config.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicProperties {

    private String userAccountTopic;

    private String userTokenTopic;

    private String userTokenTransactionTopic;

    private String tokenTopic;

    private String userTopic;

    private String userTokenUpdateByTransactionTopic;

    private String tokenPriceTopic;
}
