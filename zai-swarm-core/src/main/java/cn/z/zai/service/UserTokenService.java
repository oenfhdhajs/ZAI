package cn.z.zai.service;

import cn.z.zai.dto.entity.UserToken;
import cn.z.zai.dto.vo.UserTokenVo;

import java.math.BigInteger;
import java.util.List;

public interface UserTokenService {

    UserTokenVo queryByAddressAndTgUserId(BigInteger tgUserId, String address);

    List<UserTokenVo> getTokenList(BigInteger tgUserId, Boolean containSol);


    void insertTokenList(List<UserToken> tokenList, BigInteger tgUserId);
}
