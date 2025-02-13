package cn.z.zai.dto.so;

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
public class TokenTendencyMaxSo {

    private Integer id;

    private String address;

    private BigDecimal price;

    private BigDecimal openPrice;
    
    private BigDecimal highPrice;
    
    private BigDecimal lowPrice;
    
    private BigDecimal closePrice;
    
    private LocalDate currentDay;

    private BigInteger lastTimestamp;

    private LocalDateTime lastDateTime;

    private LocalDate day;
}
