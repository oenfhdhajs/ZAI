package cn.z.zai.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
public class TokenSecurityDetail extends BaseEntity {
    private static final long serialVersionUID = 6036024605917315369L;

    /**
     *
     */
    private String address;

    /**
     *  0:no 1:neltral 2warning 3:danger
     */
    private Integer alarm;

    /**
     *  contractVerified
     */
    private Boolean contractVerified;

    /**
     *  tokenDynamicDetails.is_Honeypot
     */
    private Boolean honeypot;

    /**
     *  Buy Tax tokenDynamicDetails.buy_Tax
     */
    private String buyTax;

    /**
     *  Sell Tax tokenDynamicDetails.sell_Tax
     */
    private String sellTax;

    /**
     *  Proxy Contract quickiAudit.is_Proxy
     */
    private Boolean proxyContract;

    /**
     *  Mintable quickiAudit.can_Mint
     */
    private Boolean mintable;

    /**
     * Tax Modifiable quickiAudit.can_Update_Fees
     */
    private Boolean taxModifiable;

    /**
     *  Transfer Pausable quickiAudit.has_ModifiedTransfer_Warning
     */
    private Boolean transferPausable;

    /**
     *  Blacklisted quickiAudit.can_Blacklist
     */
    private Boolean blacklisted;

    /**
     *  Scam risk isScam  Quick Intel
     */
    private Boolean scamRisk;

    /**
     * Retrieve Ownership
     */
    private Boolean retrieveOwnership;

    /**
     *  Balance Modifiable
     */
    private Boolean balanceModifiable;

    /**
     * Hidden Owner quickiAudit.hidden_​​Owner
     */
    private Boolean hiddenOwner;

    /**
     * Selfdestruct quickiAudit.can_Burn
     */
    private Boolean selfdestruct;

    /**
     *   External Call Risk quickiAudit.has_External_Functions
     *
     */
    private Boolean externalCallRisk;

    /**
     *   Buy Available tokenDynamicDetails.problem
     */
    private Boolean buyAvailable;

    /**
     * Max Sell Ratio tokenDynamicDetails.max_Transaction_Percent
     */
    private String maxSellRatio;

    /**
     *  Whitelisted quickiAudit.can_Whitelist
     */
    private Boolean whitelisted;

    /**
     * Antiwhale Antiwhale tokenDynamicDetails.max_Transaction
     */
    private Integer antiwhale;

    /**
     *  Antiwhale Modifiable
     */
    private String antiwhaleModifiable;

    /**
     *  Trading Cooldown quickiAudit.has_Trading_Cooldown
     */
    private Boolean tradingCooldown;

    /**
     *  Personal Tax Modifiable
     */
    private String personalTaxModifiable;

    /**
     *  Real/Fake Token
     */
    private String realFakeToken;

    /**
     * Airdrop Scam isAirdropPhishingScam
     */
    private Boolean airdropScam;

    /**
     *  Can Be Trusted
     */
    private Boolean canBeTrusted;

    /**
     *  Owner Address tokenDetails.tokenOwner /quickiAudit.contract_Owner
     */
    private String ownerAddress;

    /**
     *  Owner Balance
     */
    private String ownerBalance;

    /**
     * Owner Percent
     */
    private String ownerPercent;

    /**
     *  Creator Address quickiAudit.contract_Creator
     */
    private String creatorAddress;

    /**
     *  Creator Balance
     */
    private String creatorBalance;

    /**
     *  Creator Percent
     */
    private String creatorPercent;

    /**
     *  Pair Holders
     */
    private String pairHolders;

    /**
     *  Other Risks
     */
    private String otherRisks;

    /**
     *  Note
     */
    private String note;

    /**
     * has_Scams
     */
    private Boolean hasScams;

    /**
     *
     *
     */
    private String transferTax;

    /**
     *
     *
     */
    private String lpBurnedPercent;

    /**
     *
     */
    private Boolean contractRenounced;

    /**
     *
     */
    private Boolean canFreezeTrading;

    /**
     *
     */
    private Boolean mutable;

    /**
     *
     */
    private Boolean authorities;

    /**
     *
     */
    private BigDecimal top10HolderPercent;

}
