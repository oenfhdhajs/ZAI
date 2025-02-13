package cn.z.zai.dto.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;


@Data
public class UserWalletError implements Serializable {
    private static final long serialVersionUID = 105825737463607982L;

    private Integer id;

    private BigInteger tgUserId;

    private Integer num;


    private Integer incNum;
}
