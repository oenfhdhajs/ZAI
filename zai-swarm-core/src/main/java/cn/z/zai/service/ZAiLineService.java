package cn.z.zai.service;

import cn.z.zai.dto.entity.ZAiLine;

import java.math.BigInteger;
import java.util.List;

public interface ZAiLineService {

    List<ZAiLine> queryByTgUserId(BigInteger tgUserId);

    void addZAi(ZAiLine zAiLine);

    ZAiLine existMessageId(String messageId);

    void delBuyMessageId(String messageId);
}
