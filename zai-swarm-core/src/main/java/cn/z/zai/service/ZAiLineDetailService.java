package cn.z.zai.service;

import cn.z.zai.dto.entity.ZAiLineDetail;

import java.util.List;


public interface ZAiLineDetailService {

    void addZAiDetail(ZAiLineDetail zAiLineDetail);

    List<ZAiLineDetail> queryByMessageId(String messageId);
}
