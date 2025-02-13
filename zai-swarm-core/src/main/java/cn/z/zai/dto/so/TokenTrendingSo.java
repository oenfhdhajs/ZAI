package cn.z.zai.dto.so;

import java.math.BigInteger;
import java.time.LocalDateTime;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TokenTrendingSo extends BasePageSo {

    private Integer id;

    private String name;

    private String address;

    private String symbol;

    private Integer decimals;

    private BigInteger lastTimestamp;

    private LocalDateTime lastDateTime;
}
