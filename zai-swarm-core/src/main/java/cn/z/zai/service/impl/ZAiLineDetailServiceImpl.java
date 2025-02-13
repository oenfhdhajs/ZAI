package cn.z.zai.service.impl;

import cn.z.zai.dao.ZAiLineDetailDao;
import cn.z.zai.dto.entity.ZAiLineDetail;
import cn.z.zai.service.ZAiLineDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class ZAiLineDetailServiceImpl implements ZAiLineDetailService {
    @Autowired
    private ZAiLineDetailDao dao;

    @Override
    public void addZAiDetail(ZAiLineDetail zAiLineDetail) {
        dao.addZAiDetail(zAiLineDetail);
    }

    @Override
    public List<ZAiLineDetail> queryByMessageId(String messageId) {
        return dao.queryByMessageId(messageId);
    }
}
