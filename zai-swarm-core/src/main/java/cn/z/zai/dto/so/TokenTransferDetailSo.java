package cn.z.zai.dto.so;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenTransferDetailSo {

    private String sourceTransactionHash;

    private String sourceSymbol;

    private BigInteger sourceAmount;

    private BigInteger tgUserId;

    private String toAddress;

    private BigInteger usdtAmount;

    private String transactionHash;

    private Integer status;

    private Integer retryTime;

    private Integer retryTimeMin;

    private Integer retryTimeMax;

}
