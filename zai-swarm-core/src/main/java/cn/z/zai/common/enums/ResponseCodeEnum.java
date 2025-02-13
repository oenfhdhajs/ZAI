package cn.z.zai.common.enums;

import lombok.Data;

@Data
public class ResponseCodeEnum {

    /**
     * code
     */
    private final Integer code;
    /**
     * desc
     */
    private final String desc;

    public ResponseCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
