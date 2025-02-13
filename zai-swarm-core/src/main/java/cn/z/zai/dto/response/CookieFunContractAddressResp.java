package cn.z.zai.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class CookieFunContractAddressResp implements Serializable {
    private static final long serialVersionUID = 5948106601366966852L;

    private Boolean success;

    private String error;

    private OK ok;

    @Data
    public static class OK {

        private BigDecimal mindshare;

        private Long followersCount;

        private Long smartFollowersCount;

    }

}
