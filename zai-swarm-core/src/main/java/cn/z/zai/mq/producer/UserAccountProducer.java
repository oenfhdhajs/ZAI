package cn.z.zai.mq.producer;

import cn.z.zai.config.mq.KafkaTopicProperties;
import cn.z.zai.dto.vo.kafka.KafkaSendMsgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;


@Slf4j
@Component
public class UserAccountProducer {

    @Autowired
    private KafkaStandardProducer kafkaStandardProducer;

    @Autowired
    private KafkaTopicProperties kafkaTopicProperties;

    public void sendUserAccount(BigInteger tgUserId) {
        //WEB  sol
        KafkaSendMsgVo kafkaSendMsgVo = new KafkaSendMsgVo();
        kafkaSendMsgVo.setTopic(kafkaTopicProperties.getUserAccountTopic());
        kafkaSendMsgVo.setMessageData(tgUserId.toString());
        kafkaStandardProducer.sendMessage(kafkaSendMsgVo);
    }
}
