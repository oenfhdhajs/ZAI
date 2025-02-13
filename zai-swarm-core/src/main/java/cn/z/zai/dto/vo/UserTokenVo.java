package cn.z.zai.dto.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class UserTokenVo implements Serializable {

    private String address;

    private String name;

    private String symbol;

    private String image;

    private Long amount;

    private Integer decimals;

    private BigDecimal price;

    private BigDecimal boughtPrice;

    private BigDecimal accountIncreaseRate;

    private LocalDateTime createdTime;
}
