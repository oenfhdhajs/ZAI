package cn.z.zai.dto.request.chat;

import lombok.Data;

import java.math.BigInteger;


@Data
public class ZAIChatRequest {

    private BigInteger tgUserId;

    private String messageId;

    private String content;


    /**
     * inner use
     */
    private String oneQuestId;

}
