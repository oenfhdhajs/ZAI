package cn.z.zai.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyePriceResponse {

    /**
     * price
     */
    private BigDecimal value;

    private BigDecimal priceChange24h;

    /**
     * seconds
     */
    private Long updateUnixTime;

    private LocalDateTime updateHumanTime;

    private BigDecimal liquidity;
}
