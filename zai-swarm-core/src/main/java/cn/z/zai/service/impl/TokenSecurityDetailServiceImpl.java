package cn.z.zai.service.impl;

import cn.z.zai.dao.TokenSecurityDetailDao;
import cn.z.zai.dto.entity.TokenSecurityDetail;
import cn.z.zai.dto.request.BirdEyeTokenSecurityRequest;
import cn.z.zai.dto.response.BirdEyeTokenSecurityResponse;
import cn.z.zai.dto.response.QuickIntelResponseQuickiauditAuthorities;
import cn.z.zai.dto.response.QuickIntelResponseQuickiauditfull;
import cn.z.zai.dto.response.QuickIntelResponseQuickiauditfullQuickiAudit;
import cn.z.zai.dto.response.QuickIntelResponseQuickiauditfullTokenDetails;
import cn.z.zai.dto.response.QuickIntelResponseQuickiauditfullTokenDynamicDetails;
import cn.z.zai.service.TokenSecurityDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;


@Slf4j
@Service
public class TokenSecurityDetailServiceImpl implements TokenSecurityDetailService {

    @Autowired
    private TokenSecurityDetailDao tokenSecurityDetailDao;


    @Autowired
    private QuickIntelApi quickIntelApi;

    @Autowired
    private BirdEyeApi birdEyeApi;

    @Override
    public void insertTokenSecurity(String tokenAddress) {
        if (StringUtils.isEmpty(tokenAddress)) {
            return;
        }
        TokenSecurityDetail detail = tokenSecurityDetailDao.getTokenSecurityDetail(tokenAddress);
        if (detail != null) {
            log.info("token exit, not find {}", tokenAddress);
            return;
        }
        try {
            QuickIntelResponseQuickiauditfull tokenSecretInfo = quickIntelApi.getTokenSecretInfo((tokenAddress));
            if (tokenSecretInfo == null) {
                return;
            }
            BirdEyeTokenSecurityRequest request = new BirdEyeTokenSecurityRequest();
            request.setAddress(tokenAddress);
            BirdEyeTokenSecurityResponse birdEyeTokenSecurityResponse = birdEyeApi.tokenSecurity(request);
            TokenSecurityDetail tokenSecurityDetail = new TokenSecurityDetail();
            tokenSecurityDetail.setAddress(tokenAddress);
            buildTokenSecurityDetail(tokenSecretInfo, birdEyeTokenSecurityResponse, tokenSecurityDetail);
            dualAlarm(tokenSecurityDetail);
            tokenSecurityDetailDao.addTokenSecurityDetail(tokenSecurityDetail);
        } catch (Exception error) {
            log.error("rest tokens info exception {}", tokenAddress, error);
        }
    }


    private void buildTokenSecurityDetail(QuickIntelResponseQuickiauditfull quickiauditfull,
                                          BirdEyeTokenSecurityResponse birdEyeTokenSecurityResponse, TokenSecurityDetail tokenSecurityDetail) {

        if (Objects.isNull(quickiauditfull)) {
            return;
        }
        if (!Objects.isNull(birdEyeTokenSecurityResponse)) {
            tokenSecurityDetail.setTop10HolderPercent(birdEyeTokenSecurityResponse.getTop10HolderPercent());
        }
        QuickIntelResponseQuickiauditfullTokenDynamicDetails tokenDynamicDetails =
                quickiauditfull.getTokenDynamicDetails();
        if (Objects.isNull(tokenDynamicDetails)) {
            tokenDynamicDetails = new QuickIntelResponseQuickiauditfullTokenDynamicDetails();
        }
        QuickIntelResponseQuickiauditfullTokenDetails tokenDetails = quickiauditfull.getTokenDetails();
        if (Objects.isNull(tokenDetails)) {
            tokenDetails = new QuickIntelResponseQuickiauditfullTokenDetails();
        }

        QuickIntelResponseQuickiauditfullQuickiAudit quickiAudit = quickiauditfull.getQuickiAudit();
        if (Objects.isNull(quickiAudit)) {
            quickiAudit = new QuickIntelResponseQuickiauditfullQuickiAudit();
        }

        tokenSecurityDetail.setContractVerified(quickiauditfull.getContractVerified());
        tokenSecurityDetail.setHoneypot(tokenDynamicDetails.getIs_Honeypot());
        tokenSecurityDetail.setBuyTax(tokenDynamicDetails.getBuy_Tax());
        tokenSecurityDetail.setSellTax(tokenDynamicDetails.getSell_Tax());
        tokenSecurityDetail.setProxyContract(quickiAudit.getIs_Proxy());
        tokenSecurityDetail.setMintable(quickiAudit.getCan_Mint());
        tokenSecurityDetail.setTaxModifiable(quickiAudit.getCan_Mint());
        tokenSecurityDetail.setTransferPausable(quickiAudit.getHas_ModifiedTransfer_Warning());
        tokenSecurityDetail.setBlacklisted(quickiAudit.getCan_Blacklist());
        tokenSecurityDetail.setScamRisk(quickiauditfull.getIsScam());
        // tokenSecurityDetail.setRetrieveOwnership();
        // tokenSecurityDetail.setBalanceModifiable();
        tokenSecurityDetail.setHiddenOwner(quickiAudit.getHidden_Owner());
        tokenSecurityDetail.setSelfdestruct(quickiAudit.getCan_Burn());
        tokenSecurityDetail.setExternalCallRisk(quickiAudit.getHas_External_Functions());
        tokenSecurityDetail.setBuyAvailable(tokenDynamicDetails.getProblem());
        tokenSecurityDetail.setMaxSellRatio(tokenDynamicDetails.getMax_Transaction_Percent());
        tokenSecurityDetail.setWhitelisted(quickiAudit.getCan_Whitelist());
        tokenSecurityDetail.setAntiwhale(tokenDynamicDetails.getMax_Transaction());

        tokenSecurityDetail.setTradingCooldown(quickiAudit.getHas_Trading_Cooldown());

        tokenSecurityDetail.setAirdropScam(quickiauditfull.getIsAirdropPhishingScam());

        tokenSecurityDetail.setOwnerAddress(quickiAudit.getContract_Address());

        tokenSecurityDetail.setCreatorAddress(quickiAudit.getContract_Creator());

        tokenSecurityDetail.setHasScams(quickiAudit.getHas_Scams());
        tokenSecurityDetail.setTransferTax(tokenDynamicDetails.getTransfer_Tax());
        tokenSecurityDetail.setLpBurnedPercent(tokenDynamicDetails.getLp_Burned_Percent());
        tokenSecurityDetail.setContractRenounced(quickiAudit.getContract_Renounced());
        tokenSecurityDetail.setCanFreezeTrading(quickiAudit.getCan_Freeze_Trading());

        tokenSecurityDetail.setMutable(quickiAudit.getIs_Mutable());

        boolean authoritiesFlage = false;

        QuickIntelResponseQuickiauditAuthorities authorities = quickiAudit.getAuthorities();
        if (Objects.nonNull(authorities)) {
            if (StringUtils.isNotEmpty(authorities.getFull_Authority())
                    || StringUtils.isNotEmpty(authorities.getMint_Authority())
                    || StringUtils.isNotEmpty(authorities.getFreeze_Authority())
                    || StringUtils.isNotEmpty(authorities.getWithdraw_Authority())
                    || StringUtils.isNotEmpty(authorities.getFreeze_Authority())) {
                authoritiesFlage = true;
            }
            ;

        }

        tokenSecurityDetail.setAuthorities(authoritiesFlage);

    }

    private void dualAlarm(TokenSecurityDetail tokenSecurityDetail) {


        Boolean scamRisk = tokenSecurityDetail.getScamRisk();

        Boolean hasScams = tokenSecurityDetail.getHasScams();

        Boolean airdropScam = tokenSecurityDetail.getAirdropScam();

        Boolean honeypot = tokenSecurityDetail.getHoneypot();
        if (BooleanUtils.isTrue(scamRisk) || BooleanUtils.isTrue(airdropScam) || BooleanUtils.isTrue(hasScams)
                || BooleanUtils.isTrue(honeypot)) {
            tokenSecurityDetail.setAlarm(3);
            return;
        }


        Boolean mintable = tokenSecurityDetail.getMintable();

        Boolean blacklisted = tokenSecurityDetail.getBlacklisted();

        String transferTax = tokenSecurityDetail.getTransferTax();
        BigDecimal bigDecimal = BigDecimal.ZERO;
        try {
            if (StringUtils.isNotEmpty(transferTax)) {
                bigDecimal = new BigDecimal(transferTax);
            }

        } catch (Exception ignored) {

        }
        String lpBurnedPercent = tokenSecurityDetail.getLpBurnedPercent();

        BigDecimal lpBurnedPercentDecimal = BigDecimal.ZERO;
        try {
            if (StringUtils.isNotEmpty(lpBurnedPercent)) {
                lpBurnedPercentDecimal = new BigDecimal(lpBurnedPercent);
            } else {

                lpBurnedPercentDecimal = new BigDecimal("100");
            }
        } catch (Exception ignored) {

        }

        boolean transferTaxFlag = bigDecimal.compareTo(new BigDecimal("20")) > 0;

        boolean lpBurnedPercentFlag =
                StringUtils.isNotEmpty(lpBurnedPercent) && lpBurnedPercentDecimal.compareTo(new BigDecimal("70")) < 0;

        Boolean canFreezeTrading = tokenSecurityDetail.getCanFreezeTrading();

        Boolean taxModifiable = tokenSecurityDetail.getTaxModifiable();


        Boolean authorities = tokenSecurityDetail.getAuthorities();

        Boolean contractVerified = tokenSecurityDetail.getContractVerified();

        if (BooleanUtils.isTrue(mintable) || BooleanUtils.isTrue(blacklisted) || BooleanUtils.isTrue(transferTaxFlag)
                || BooleanUtils.isTrue(lpBurnedPercentFlag) || BooleanUtils.isTrue(canFreezeTrading)
                || BooleanUtils.isTrue(taxModifiable) || BooleanUtils.isTrue(authorities)
                || BooleanUtils.isFalse(contractVerified)) {
            tokenSecurityDetail.setAlarm(2);
            return;
        }

        tokenSecurityDetail.setAlarm(0);
    }
}
