package cn.z.zai.dto.so;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AirdropClaimSo {

    @NotNull
    private String transaction;
}
