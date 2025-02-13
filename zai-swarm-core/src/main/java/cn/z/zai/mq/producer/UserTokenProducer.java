package cn.z.zai.mq.producer;

import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.config.mq.KafkaTopicProperties;
import cn.z.zai.dto.vo.kafka.KafkaSendMsgVo;
import cn.z.zai.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class UserTokenProducer {

    @Autowired
    private KafkaStandardProducer kafkaStandardProducer;

    @Autowired
    private KafkaTopicProperties kafkaTopicProperties;

    @Autowired
    private RedisUtil redisUtil;

    public void sendUserToken(BigInteger tgUserId) {
        if (!redisUtil.setIfAbsent(String.format(RedisCacheConstant.USER_TOKEN_LIST_CONSUME, tgUserId), "1", 30,
                TimeUnit.SECONDS)) {
            log.info("kafka send token update limit {}", tgUserId);
            return;
        }

        KafkaSendMsgVo kafkaSendMsgVo = new KafkaSendMsgVo();
        kafkaSendMsgVo.setTopic(kafkaTopicProperties.getUserTokenTopic());
        kafkaSendMsgVo.setMessageData(tgUserId.toString());
        kafkaStandardProducer.sendMessage(kafkaSendMsgVo);
    }
}
