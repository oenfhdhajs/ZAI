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
public class UserTransactionLogSo{

    private BigInteger tgUserId;

    private String transId;

    private Integer status;

    private Integer retryTime;

    private Integer retryTimeMin;

    private Integer retryTimeMax;

}
