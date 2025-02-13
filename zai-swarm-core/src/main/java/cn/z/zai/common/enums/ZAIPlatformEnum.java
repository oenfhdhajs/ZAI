package cn.z.zai.common.enums;

import lombok.Getter;

@Getter
public enum ZAIPlatformEnum {

    CHAT_GPT(0, "chatGpt"),



    ;

    private final Integer type;
    private final String message;

    ZAIPlatformEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }
}
