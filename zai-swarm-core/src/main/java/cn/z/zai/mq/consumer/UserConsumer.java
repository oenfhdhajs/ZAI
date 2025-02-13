package cn.z.zai.mq.consumer;

import cn.z.zai.dto.vo.KafkaUserMsgVo;
import cn.z.zai.service.UserMqProcessAbstract;
import cn.z.zai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class UserConsumer implements InitializingBean {
    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private List<UserMqProcessAbstract> rdsProcessAbstract;

    private static final Map<String, UserMqProcessAbstract> INNER_ABSTRACT_MAP = new HashMap<>();

    @KafkaListener(id = "mqUserProcessConsumer", groupId = "${kafka.group.userGroup}",
        topics = {"${kafka.topic.userTopic}"}, concurrency = "1")
    public void rdsProcess(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        if (!CollectionUtils.isEmpty(records)) {
            log.info("UserConsumer size is {}", records.size());
        }

        for (ConsumerRecord<String, String> record : records) {
            String message = record.value();

            log.info("receive messages mqUserProcessConsumer -> {}", message);
            KafkaUserMsgVo kafkaRdsMsgVo = jsonUtil.string2Obj(message, KafkaUserMsgVo.class);

            UserMqProcessAbstract rdsProcessAbstractApi = INNER_ABSTRACT_MAP.get(kafkaRdsMsgVo.getType());

            rdsProcessAbstractApi.process(kafkaRdsMsgVo);
        }

        ack.acknowledge();
    }

    @Override
    public void afterPropertiesSet() {
        if (CollectionUtils.isEmpty(rdsProcessAbstract)) {
            return;
        }
        if (CollectionUtils.isEmpty(INNER_ABSTRACT_MAP)) {
            for (UserMqProcessAbstract abstracts : rdsProcessAbstract) {
                INNER_ABSTRACT_MAP.put(abstracts.type(), abstracts);
            }
        }

    }

}
