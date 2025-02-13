package cn.z.zai.config.mq;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
    @Autowired
    private KafkaProducerProperties kafkaProducerProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean("kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrapServers());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerProperties.getRetries());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaProducerProperties.getMaxBlockMs());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerProperties.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperties.getLingerMs());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProducerProperties.getBufferMemory());
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,kafkaProducerProperties.getMaxRequestSize());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getValueSerializer());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"none");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, kafkaProducerProperties.getPartitionerClass());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperties.getAcks());
        return props;
    }

}
