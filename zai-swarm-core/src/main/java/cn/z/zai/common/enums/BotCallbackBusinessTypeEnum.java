package cn.z.zai.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum BotCallbackBusinessTypeEnum {

    UN_KNOWN(0,"unknown"),

    START(1,"/start"),

    FRIEND(2,"/start r_"),

    PAY_SUPPORT(3, "/paysupport"),

    OTHER(100,"other");

    ;

    private int type;

    private String desc;
}
