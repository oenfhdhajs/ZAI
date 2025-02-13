package cn.z.zai.common.enums;

import lombok.Getter;

@Getter
public enum ZAIModelEnum {

    CHAT_GPT(0, "chatGpt"),



    ;

    private final Integer type;
    private final String message;

    ZAIModelEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }
}
