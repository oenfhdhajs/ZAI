package cn.z.zai.dto.entity;

import cn.z.zai.common.enums.ZAIModelEnum;
import cn.z.zai.common.enums.ZAIPlatformEnum;
import cn.z.zai.common.enums.ZAITypeEnum;
import cn.z.zai.dto.request.chat.ZAIBaseChatContent;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;


@Data
public class ZAiLineDetail extends BaseEntity {

    private static final long serialVersionUID = 5832745491818043684L;

    private BigInteger tgUserId;

    private String messageId;

    private String oneQuestId;

    /**
     * @see ZAIPlatformEnum
     */
    private Integer platform;

    /**
     * @see ZAIModelEnum
     */
    private Integer model;

    /**
     * @see ZAITypeEnum
     */
    private Integer type;

    /**
     * @see cn.z.zai.dto.request.chat.ChatMessageRole
     */
    private String role;

    private String content;

    private String showContent;

    private List<ZAIBaseChatContent> chatContent;
}
