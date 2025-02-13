package cn.z.zai.service;


import java.math.BigInteger;


public interface UserTokenSyncService {

    void syncUserAccount(BigInteger tgUserId, String address);

    void syncUserTokenListNew(BigInteger tgUserId, String walletAddress);


}
