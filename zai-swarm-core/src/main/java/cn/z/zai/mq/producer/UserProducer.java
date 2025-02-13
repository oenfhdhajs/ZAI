package cn.z.zai.mq.producer;


import cn.z.zai.common.enums.KafkaUserMsgTypeEnum;
import cn.z.zai.config.mq.KafkaTopicProperties;
import cn.z.zai.dto.entity.User;
import cn.z.zai.dto.vo.KafkaUserMsgVo;
import cn.z.zai.dto.vo.kafka.KafkaSendMsgVo;
import cn.z.zai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class UserProducer {

    @Autowired
    private KafkaStandardProducer mqProducer;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private KafkaTopicProperties kafkaTopicProperties;

    public void sendUserInfo(User clicker) {

        send(jsonUtil.obj2String(jsonUtil.obj2String(clicker)), KafkaUserMsgTypeEnum.USER_INFO);
    }


    private void send(String jsonMsg, KafkaUserMsgTypeEnum kafkaUserMsgTypeEnum) {
        KafkaUserMsgVo kafkaUserMsgVo = new KafkaUserMsgVo();
        kafkaUserMsgVo.setType(kafkaUserMsgTypeEnum.getType());
        kafkaUserMsgVo.setUserData(jsonMsg);

        KafkaSendMsgVo kafkaSendMsgVo = new KafkaSendMsgVo();
        kafkaSendMsgVo.setTopic(kafkaTopicProperties.getUserTopic());
        kafkaSendMsgVo.setMessageData(jsonUtil.obj2String(kafkaUserMsgVo));
        mqProducer.sendMessage(kafkaSendMsgVo);
    }
}
