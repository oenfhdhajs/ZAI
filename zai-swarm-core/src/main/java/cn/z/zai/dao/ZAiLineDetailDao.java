package cn.z.zai.dao;

import cn.z.zai.dto.entity.ZAiLineDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ZAiLineDetailDao {

    void addZAiDetail(ZAiLineDetail zAiLineDetail);

    List<ZAiLineDetail> queryByMessageId(@Param("messageId") String messageId);


    List<ZAiLineDetail> queryByOneQuestIdId(@Param("oneQuestId") String oneQuestId);

    void updateTransactionStatus(ZAiLineDetail zAiLineDetail);
}
