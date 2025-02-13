package cn.z.zai.service;


import cn.z.zai.dto.vo.KafkaUserMsgVo;

public interface UserMqProcessService {

    void process(KafkaUserMsgVo kafkaUserMsgVo);
}
