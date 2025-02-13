package cn.z.zai.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiKeyLevelEnum {

    DEFAULT(0),

    PRIMARY(1);

    private final Integer level;

}
