package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyeTokenOverviewResponse {

    /**
     * Address of a token
     */
    private String address;

    private Integer decimals;

    private String symbol;

    private String name;

    private String logoURI;

    private BigDecimal liquidity;

    private BigDecimal price;

    private BigDecimal history24hPrice;

    private BigDecimal priceChange24hPercent;

    private BigDecimal supply;

    private BigDecimal mc;

    private BigDecimal circulatingSupply;

    private BigDecimal realMc;

    private BigInteger holder;

    private BigDecimal v24hUSD;

    private BirdEyeTokenOverviewResponseItemExtensions extensions;
}
