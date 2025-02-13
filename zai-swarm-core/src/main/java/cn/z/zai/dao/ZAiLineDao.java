package cn.z.zai.dao;

import cn.z.zai.dto.entity.ZAiLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Mapper
@Repository
public interface ZAiLineDao {

    List<ZAiLine> queryByTgUserId(@Param("tgUserId") BigInteger tgUserId);

    void addZAi(ZAiLine zAiLine);

    ZAiLine existMessageId(@Param("messageId")String messageId);

    void delBuyMessageId(@Param("tgUserId") BigInteger tgUserId, @Param("messageId")String messageId);
}
