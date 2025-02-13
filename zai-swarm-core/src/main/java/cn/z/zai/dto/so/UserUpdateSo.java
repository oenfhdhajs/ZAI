package cn.z.zai.dto.so;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class UserUpdateSo {

    private String salt;

    private String verifyCode;

    @NotNull
    private String address;
}
