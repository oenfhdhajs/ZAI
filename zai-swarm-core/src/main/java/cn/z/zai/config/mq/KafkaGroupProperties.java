package cn.z.zai.config.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "kafka.group")
public class KafkaGroupProperties {

    private String userAccountGroup;

    private String userTokenGroup;

    private String userTokenTransactionGroup;
}