package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyeOHLCVResponseItem {

    private BigDecimal o;

    private BigDecimal h;

    private BigDecimal l;

    private BigDecimal c;

    private BigDecimal v;

    private Long unixTime;

    private String address;

    private String type;
}
