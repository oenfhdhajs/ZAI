package cn.z.zai.common.enums;

import lombok.Getter;

@Getter
public enum UserSourceType implements EnumInterface{

    TELEGRAM(0, "Telegram"),

    WEB_BOT(1, "WEB_BOT"),

    ;

    private final Integer code;
    private final String message;

    UserSourceType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
