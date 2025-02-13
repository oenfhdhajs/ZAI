package cn.z.zai.service.impl;

import cn.z.zai.dao.ZAiLineDao;
import cn.z.zai.dto.entity.ZAiLine;
import cn.z.zai.service.ZAiLineService;
import cn.z.zai.util.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;


@Slf4j
@Service
public class ZAiLineServiceImpl implements ZAiLineService {
    @Autowired
    private ZAiLineDao dao;

    @Override
    public List<ZAiLine> queryByTgUserId(BigInteger tgUserId) {
        return dao.queryByTgUserId(tgUserId);
    }

    @Override
    public void addZAi(ZAiLine zAiLine) {
        dao.addZAi(zAiLine);
    }

    @Override
    public ZAiLine existMessageId(String messageId) {
        return dao.existMessageId(messageId);
    }

    @Override
    public void delBuyMessageId(String messageId) {
        dao.delBuyMessageId(ContextHolder.getUserId(), messageId);
    }
}
