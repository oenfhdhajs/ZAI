package cn.z.zai.dto.response;

import lombok.Data;


@Data
public class QuickIntelResponseQuickiauditfullTokenDetails {

    // The name of the contract
    private String tokenName;

    // The symbol of the contract/token.
    private String tokenSymbol;

    // The number of decimal places the contract uses.
    private Integer tokenDecimals;

    // The current token owner's address.
    private String tokenOwner;

    // The total token supply.
    private Long tokenSupply;

    // The timestamp of when the token was created.
    private Integer tokenCreatedDate;

    private QuickiTokenHash quickiTokenHash;

    @Data
    private static class QuickiTokenHash {

        // The hash for exact contracts.
        private String exact_qHash;

        // The hash for similar contracts.
        private String similar_qHash;
    }
}