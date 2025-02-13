package cn.z.zai.service;

import cn.z.zai.dto.vo.SmarterTonBalanceMessage;

public interface SmartWalletService {


    SmarterTonBalanceMessage buildSendMsg4WebBot(String tonAddress);


    String buildSmartSearchAddress4WebBot(SmarterTonBalanceMessage smarterTonBalanceMessage);
}
