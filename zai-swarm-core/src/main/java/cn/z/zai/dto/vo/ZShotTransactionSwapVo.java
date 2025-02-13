package cn.z.zai.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZShotTransactionSwapVo {

    /**
     *
     */
    private String ownerAddress;

    /**
     *
     */
    private String inputMintAddress;

    /**
     * inputMintAddress
     */
    private Integer inputMintDecimals;

    /**
     * inputMintAddress
     */
    private Long amount;

    /**
     * inputMintAddress
     */
    private Double price;

    /**
     *
     */
    private String outputMintAddress;

    /**
     *
     * 500 = 5%
     */
    private Integer slippageBps;

    /**
     *
     */
    private Boolean isMax;

    private BigInteger tgUserId;
}