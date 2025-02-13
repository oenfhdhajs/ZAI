package cn.z.zai.dto.so;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class UserAddressUpdateSo {

    @NotNull
    private String address;

    @NotNull
    private String salt;

    private String code;

    private Boolean isLog;
}
