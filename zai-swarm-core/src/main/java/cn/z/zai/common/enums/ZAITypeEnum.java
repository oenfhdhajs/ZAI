package cn.z.zai.common.enums;

import lombok.Getter;

@Getter
public enum ZAITypeEnum {

    question(0, "question"),

    answer(1, "answer"),

    ;

    private final Integer type;
    private final String message;

    ZAITypeEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }
}
