package cn.z.zai.dto.so;

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
public class TonAccountTransactionSo {

    private BigInteger tgUserId;
    
    private LocalDate day;
    
    private String sourceAddress;
    
    private String targetAddress;
    
    private String hash;

    private String traceId;
    
    private String currentLt;

    private BigInteger value;

    private BigInteger jettonValue;

    private String jettonType;

    private String opcode;
    
    private LocalDateTime now;

    /**
     *
     * default:0
     */
    private Integer status;
       
}
