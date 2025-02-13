package cn.z.zai.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserToken extends BaseEntity{

    private BigInteger tgUserId;

    private String address;

    private Long amount;
}
