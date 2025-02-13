package cn.z.zai.dto.so;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
public class ZShotTransactionSo {

    @NotNull
    private String transaction;

    private BigInteger tgUserId;
}
