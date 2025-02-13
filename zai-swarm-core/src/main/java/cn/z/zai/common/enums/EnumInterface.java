package cn.z.zai.common.enums;

public interface EnumInterface {

    Integer getCode();

    default String getDesc() {
        return null;
    }

}
