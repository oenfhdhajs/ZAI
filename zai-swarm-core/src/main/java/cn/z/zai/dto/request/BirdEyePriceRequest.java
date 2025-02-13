package cn.z.zai.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyePriceRequest {

  private BigDecimal check_liquidity;

  private Boolean include_liquidity;

  private String address;
}
