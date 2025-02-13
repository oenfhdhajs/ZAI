package cn.z.zai.dto.entity;

import cn.z.zai.common.enums.ZAIModelEnum;
import cn.z.zai.common.enums.ZAIPlatformEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.time.LocalDate;


@Data
@EqualsAndHashCode
public class ZAiLine extends BaseEntity {
    private static final long serialVersionUID = 5138174232932507835L;

    private LocalDate day;

    private BigInteger tgUserId;

    private String messageId;

    /**
     * @see ZAIPlatformEnum
     */
    private Integer platform;

    /**
     * @see ZAIModelEnum
     */
    private Integer model;

    private String title;
}
