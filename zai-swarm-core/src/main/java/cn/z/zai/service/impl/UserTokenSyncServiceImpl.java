package cn.z.zai.service.impl;

import cn.z.zai.dto.entity.UserToken;
import cn.z.zai.dto.response.QuickNodeResponseBalance;
import cn.z.zai.dto.response.QuickNodeResponseTokenAccountsByOwnerItemValueAccountDataParsedInfo;
import cn.z.zai.service.TokenSyncService;
import cn.z.zai.service.UserService;
import cn.z.zai.service.UserTokenService;
import cn.z.zai.service.UserTokenSyncService;
import cn.z.zai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class UserTokenSyncServiceImpl implements UserTokenSyncService {


    @Autowired
    private JsonUtil jsonUtil;


    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private TokenSyncService tokenSyncService;


    @Autowired
    private QuickNodeApi quickNodeApi;


    @Override
    public void syncUserAccount(BigInteger tgUserId, String address) {
        QuickNodeResponseBalance response = quickNodeApi.getBalance(address);
        if (response == null) {
            log.info("user wallet balance Sol is 0,tgUserId={},address={}", tgUserId, address);
            return;
        }
        if (Objects.isNull(response.getValue())) {
            return;
        }

        userService.updateLamports(tgUserId, response.getValue());
    }

    @Override
    public void syncUserTokenListNew(BigInteger tgUserId, String walletAddress) {
        if (StringUtils.isEmpty(walletAddress) || tgUserId == null) {
            return;
        }
        List<QuickNodeResponseTokenAccountsByOwnerItemValueAccountDataParsedInfo> list = tokenSyncService.syncAccountTokens(walletAddress);
        if (CollectionUtils.isNotEmpty(list)) {
            List<UserToken> userTokenList = new ArrayList<>();
            for (QuickNodeResponseTokenAccountsByOwnerItemValueAccountDataParsedInfo item : list) {
                if (item == null || !walletAddress.equals(item.getOwner()) || item.getTokenAmount() == null
                        || StringUtils.isEmpty(item.getTokenAmount().getAmount())) {
                    log.info("[syncAccountTokens] quickNode response item is null,walletAddress={},item={}", walletAddress, jsonUtil.obj2String(item));
                    continue;
                }
                UserToken userToken = new UserToken();
                userToken.setTgUserId(tgUserId);
                userToken.setAddress(item.getMint());
                userToken.setAmount(Long.parseLong(item.getTokenAmount().getAmount()));
                userTokenList.add(userToken);
            }
            if (userTokenList.size() > 0) {
                userTokenService.insertTokenList(userTokenList, tgUserId);

            }
        }
    }


}
