package cn.z.zai.dto.response;

import lombok.Data;

import java.util.List;


@Data
public class QuickIntelResponseQuickiauditfull {

    // Indicates if the contract is was manually confirmed as scam by thte Quick Intel team.
    private Boolean isScam;

    // Indicates if the token is an airdropped or phishing token.
    private Boolean isAirdropPhishingScam;

    // Indicates if the contract is verified.
    private Boolean contractVerified;

    // Indicates if the project has been verified by Quick Intel.
    private Boolean projectVerified;

    //  Description of the VeriFi'd project.
    private String projectVerifiDescription;

    // An array of verified KYC data of the project.
    private List<Object> kycVerifications;

    // An array of verified external audit data of the project.
    private List<Object> externalAudits;

    // An array of verified links related to the project.
    private List<Object> extraLinks;

    private QuickIntelResponseQuickiauditfullQuickiAudit quickiAudit;

    private QuickIntelResponseQuickiauditfullTokenDetails tokenDetails;

    private QuickIntelResponseQuickiauditfullTokenDynamicDetails tokenDynamicDetails;
}