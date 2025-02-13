package cn.z.zai.mq.producer;

import cn.z.zai.dto.vo.kafka.KafkaSendMsgVo;
import cn.z.zai.util.ContextHolder;
import cn.z.zai.util.JsonUtil;
import java.math.BigInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaStandardProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private JsonUtil jsonUtil;

    public void sendMessage(KafkaSendMsgVo kafkaSendMsgVo) {
        try {
            String key = String.valueOf(System.currentTimeMillis());
            BigInteger userId = ContextHolder.getUserId();
            if (userId != null) {
                key = userId.toString();
            }
            log.info("kafka send msg topic:{},data:{} {}", kafkaSendMsgVo.getTopic(), kafkaSendMsgVo.getMessageData(), key);
            kafkaTemplate.send(kafkaSendMsgVo.getTopic(), key, kafkaSendMsgVo.getMessageData());
        } catch (Exception e) {
            log.error("send error:{}", jsonUtil.obj2String(kafkaSendMsgVo), e);
        }
    }
}
