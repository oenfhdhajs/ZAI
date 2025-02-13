
package cn.z.zai.dto.so;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenTendencySo implements Serializable {

    private String address;

    private BigDecimal price;

    private Integer currentMinute;

    private Integer currentHour;

    private LocalDate currentDay;

    private BigInteger lastTimestamp;

    private LocalDateTime lastDateTime;
}
