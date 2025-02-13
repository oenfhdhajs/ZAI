package cn.z.zai.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyeCreationTokenInfoResponse {

    private String txHash;

    private Long slot;

    private String tokenAddress;

    private Integer decimals;

    private String owner;

    private Long blockUnixTime;

    private LocalDateTime blockHumanTime;
}

