package cn.z.zai.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum KafkaUserMsgTypeEnum {

    UNKNOWN("unknown"),


    USER_INFO("userInfo"),


    ;

    private final String type;

    public static KafkaUserMsgTypeEnum get(String type){
        for (KafkaUserMsgTypeEnum value : KafkaUserMsgTypeEnum.values()) {
            if (value.getType().equals(type)){
                return value;
            }
        }
        return UNKNOWN;
    }
}
