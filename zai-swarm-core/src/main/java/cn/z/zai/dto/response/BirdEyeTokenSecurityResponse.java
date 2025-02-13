package cn.z.zai.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @Description: example
 * {
 *    "creatorAddress": "DTQQf6xhbRFqbSUzHsQ4e1PJroCR3dVKvUnt7sj11HJc",
 *     "creatorOwnerAddress": null,
 *     "ownerAddress": null,
 *     "ownerOfOwnerAddress": null,
 *     "creationTx": "4TVAmKPPHWCPCykEbXNUDuVJ8fUL8rh3hyCs8TF2sMwf3RHHnfRrJ7nY8yFQvrxpGSRt1ukq7r8v7iJLGQFWQeo2",
 *     "creationTime": 1728174672,
 *     "creationSlot": 293942274,
 *     "mintTx": "4TVAmKPPHWCPCykEbXNUDuVJ8fUL8rh3hyCs8TF2sMwf3RHHnfRrJ7nY8yFQvrxpGSRt1ukq7r8v7iJLGQFWQeo2",
 *     "mintTime": 1728174672,
 *     "mintSlot": 293942274,
 *     "creatorBalance": 75.259887,
 *     "ownerBalance": null,
 *     "ownerPercentage": null,
 *     "creatorPercentage": 7.526254913687762e-8,
 *     "metaplexUpdateAuthority": "TSLvdd1pWpHVjahSpsvCXUbgwsL3JAcvokwaKt1eokM",
 *     "metaplexOwnerUpdateAuthority": "11111111111111111111111111111111",
 *     "metaplexUpdateAuthorityBalance": null,
 *     "metaplexUpdateAuthorityPercent": null,
 *     "mutableMetadata": false,
 *     "top10HolderBalance": 269807011.265157,
 *     "top10HolderPercent": 0.26981655503705404,
 *     "top10UserBalance": 252814342.87280497,
 *     "top10UserPercent": 0.25282328557006617,
 *     "isTrueToken": null,
 *     "fakeToken": null,
 *     "totalSupply": 999964628.66448,
 *     "preMarketHolder": [],
 *     "lockInfo": null,
 *     "freezeable": null,
 *     "freezeAuthority": null,
 *     "transferFeeEnable": null,
 *     "transferFeeData": null,
 *     "isToken2022": false,
 *     "nonTransferable": null,
 *     "jupStrictList": true
 * }
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyeTokenSecurityResponse {

    private String creatorAddress;

    private String creatorOwnerAddress;

    private String ownerOfOwnerAddress;

    private String creationTx;

    private Long creationTime;

    private Long creationSlot;

    private String mintTx;

    private Long mintTime;

    private Long mintSlot;

    private BigDecimal creatorBalance;

    private BigDecimal ownerBalance;

    private BigDecimal ownerPercentage;

    private BigDecimal creatorPercentage;

    private String metaplexUpdateAuthority;

    private String metaplexOwnerUpdateAuthority;

    private BigDecimal metaplexUpdateAuthorityBalance;

    private BigDecimal metaplexUpdateAuthorityPercent;

    private Boolean mutableMetadata;

    private BigDecimal top10HolderBalance;

    private BigDecimal top10HolderPercent;

    private BigDecimal top10UserBalance;

    private BigDecimal top10UserPercent;

    private Object isTrueToken;

    private Object fakeToken;

    private BigDecimal totalSupply;

    private Object preMarketHolder;

    private Object lockInfo;

    private Object freezeable;

    private Object freezeAuthority;

    private Object transferFeeEnable;

    private Object transferFeeData;

    private Boolean isToken2022;

    private Object nonTransferable;

    private Boolean jupStrictList;
}
