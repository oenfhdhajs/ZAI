package cn.z.zai.dto.so;

import lombok.Data;


@Data
public class UserInnerTransactionSo {

    private String txId;

    private Integer type;

    private String fromAddress;

    private String toAddress;

    private String fromMintAddress;

    private Long fromMintAmount;

    private String toMintAddress;

    private Long toMintAmount;
}
