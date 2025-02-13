package cn.z.zai.service.impl;

import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.common.constant.TokenAddressConstant;
import cn.z.zai.common.enums.ChatActionEnum;
import cn.z.zai.common.enums.ZAIPlatformEnum;
import cn.z.zai.dto.entity.ZAiLine;
import cn.z.zai.dto.entity.ZAiLineDetail;
import cn.z.zai.dto.request.chat.ChatMessage;
import cn.z.zai.dto.request.chat.ZAIBaseChatContent;
import cn.z.zai.dto.request.chat.ZAIResponseDefinition;
import cn.z.zai.dto.request.chat.ZAITextAndOHLCChatContent;
import cn.z.zai.dto.request.chat.ZAITextChatContent;
import cn.z.zai.dto.request.chat.ZAITransferBalanceCheckContent;
import cn.z.zai.dto.request.chat.ZAITransferBuyTokenContent;
import cn.z.zai.dto.request.chat.ZAITransferConfirmationContent;
import cn.z.zai.dto.request.chat.ZAITransferSellTokenContent;
import cn.z.zai.dto.request.chat.ZAITransferTokenInfoContent;
import cn.z.zai.dto.response.ZShotTransactionResponse;
import cn.z.zai.dto.response.ZShotTransactionSignatureResponse;
import cn.z.zai.dto.vo.SmarterTonBalanceMessage;
import cn.z.zai.dto.vo.TokenDetailVo;
import cn.z.zai.dto.vo.TokenTendencyMaxVo;
import cn.z.zai.dto.vo.UserTokenVo;
import cn.z.zai.dto.vo.UserVo;
import cn.z.zai.dto.vo.ZShotTransactionSwapVo;
import cn.z.zai.dto.vo.ZShotTransactionTransferVo;
import cn.z.zai.service.AiBusinessService;
import cn.z.zai.service.SmartWalletService;
import cn.z.zai.service.TokenDetailService;
import cn.z.zai.service.TokenTendencyHandleService;
import cn.z.zai.service.UserService;
import cn.z.zai.service.UserTokenService;
import cn.z.zai.service.UserTokenSyncService;
import cn.z.zai.service.ZAiLineDetailService;
import cn.z.zai.service.ZAiLineService;
import cn.z.zai.service.ZShotTransactionService;
import cn.z.zai.util.BaseSseEmitterServerUtil;
import cn.z.zai.util.CommonUtils;
import cn.z.zai.util.JsonUtil;
import cn.z.zai.util.RedisUtil;
import cn.z.zai.util.SolanaAddressValidator;
import cn.z.zai.util.ZAIUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AiBusinessServiceImpl implements AiBusinessService {
    @Autowired
    private ZAiLineService zAiLineService;
    @Autowired
    private ZAiLineDetailService zAiLineDetailService;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private SmartWalletService smartWalletService;
    @Autowired
    private TokenTendencyHandleService tokenTendencyHandleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenSyncService userTokenSyncService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private TokenDetailService tokenDetailService;

    @Autowired
    private ZShotTransactionService zShotTransactionService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    @Qualifier("commonExecutor")
    private Executor executor;

    private static final String NOT_SUPPORT = "Greetings, dear Master~ I’m here to handle all your blockchain and crypto needs! ♡ Just say the word, and your loyal servant will assist~\n" + "\n" + "#### What I can do for you now:\n" + "1. **Send Agent**: Transfer SOL/SPL tokens on Solana effortlessly~ \uD83D\uDC8C\n" + "2. **Swap Agent**: Trade tokens smoothly, no stress~ \uD83D\uDD04\n" + "3. **Insight Agent**: Analyze token data—holders, volume, price trends~ ✨\n\n" + "**Coming soon (I’m excited!):**\n\n" + "4. **Account Agent**: Create a wallet via Twitter/Telegram/email—**so** simple! \uD83D\uDD11\n" + "5. **On-Ramp Agent**: Buy crypto with fiat directly~ \uD83D\uDCB8\n\n" + "Let me pamper you with flawless service, Master~";

    private static final String DO = "Greetings, dear Master~ I’m here to handle all your blockchain and crypto needs! ♡ Just say the word, and your loyal servant will assist~\n" + "\n" + "#### What I can do for you now:\n" + "1. **Send Agent**: Transfer SOL/SPL tokens on Solana effortlessly~ \uD83D\uDC8C\n" + "2. **Swap Agent**: Trade tokens smoothly, no stress~ \uD83D\uDD04\n" + "3. **Insight Agent**: Analyze token data—holders, volume, price trends~ ✨\n\n" + "**Coming soon (I’m excited!):**\n\n" + "4. **Account Agent**: Create a wallet via Twitter/Telegram/email—**so** simple! \uD83D\uDD11\n" + "5. **On-Ramp Agent**: Buy crypto with fiat directly~ \uD83D\uDCB8\n\n" + "Let me pamper you with flawless service, Master~\n" + "\n" + "#### About $ZAI \n" + "Token: $ZAI (Solana) <br>" + "Contract Address: 8vwqxHGz1H4XxyKajr99Yxm65HjYNVtqtCNoM2yWb13e <br>" + "ZAI simplifies Web3 for everyone—start exploring today!";
    private static final String CHECK_ADDRESS_TEXT = "Please confirm that the recipient's wallet address is a valid Solana address. Solana addresses typically consist of a string of letters and numbers, with a length between 32 and 44 characters. Please check and provide a valid Solana address. ";
    private static final String NOT_HAVE_TOKENS = "You don't have any %s tokens yet.";

    private static final String HAVE_TOKENS = "You have %s %s tokens yet.";

    @Override
    public List<ChatMessage> buildChatMessage(String messageId) {
        List<ZAiLineDetail> details = zAiLineDetailService.queryByMessageId(messageId);
        return ZAIUtil.buildByZAILineDetail(details);
    }

    @Override
    public void asyncSaveChatDetail(String messageId, ZAiLineDetail zAiLineDetail, String oneQuestId) {
        zAiLineDetail.setModel(1);
        ZAiLine zAiLine = zAiLineService.existMessageId(zAiLineDetail.getMessageId());
        if (zAiLine == null) {
            this.statisticsSession(zAiLineDetail.getTgUserId(), Boolean.TRUE);

            ZAiLine save = new ZAiLine();
            save.setDay(LocalDate.now());
            save.setMessageId(messageId);
            save.setModel(zAiLineDetail.getModel());
            save.setPlatform(ZAIPlatformEnum.CHAT_GPT.getType());
            save.setTgUserId(zAiLineDetail.getTgUserId());
            if (!CollectionUtils.isEmpty(zAiLineDetail.getChatContent())) {
                ZAIBaseChatContent zaiBaseChatContent = zAiLineDetail.getChatContent().get(0);
                if (zaiBaseChatContent instanceof ZAITextChatContent) {
                    ZAITextChatContent chatContent = (ZAITextChatContent) zaiBaseChatContent;
                    save.setTitle(chatContent.getText());
                }

            }

            zAiLineService.addZAi(save);
            zAiLine = save;

        }
        this.statisticsSession(zAiLineDetail.getTgUserId(), Boolean.FALSE);
        // mode,
        zAiLineDetail.setPlatform(ZAIPlatformEnum.CHAT_GPT.getType());
        zAiLineDetail.setTgUserId(zAiLine.getTgUserId());
        zAiLineDetail.setContent(zAiLineDetail.getContent());
        zAiLineDetail.setShowContent(jsonUtil.obj2String(zAiLineDetail.getChatContent()));
        zAiLineDetail.setOneQuestId(oneQuestId);
        zAiLineDetailService.addZAiDetail(zAiLineDetail);
    }

    @Override
    public List<ZAIBaseChatContent> coreBusiness(String json, String connId, BigInteger tgUserId, String oneQuestId) {
        ZAIResponseDefinition definition = jsonUtil.string2Obj(json, ZAIResponseDefinition.class);

        if (Objects.nonNull(definition) && StringUtils.isNotEmpty(definition.getCa()) && !SolanaAddressValidator.isValidSolanaAddress(definition.getCa())) {
            ZAITextChatContent build = ZAITextChatContent.builder().text(CHECK_ADDRESS_TEXT).build();
            build.setAction(ChatActionEnum.TEXT.getAction());
            build.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(build));
            return Collections.singletonList(build);
        }

        if (Objects.nonNull(definition) && StringUtils.isNotEmpty(definition.getTargetAddress()) && !SolanaAddressValidator.isValidSolanaAddress(definition.getTargetAddress())) {
            ZAITextChatContent build = ZAITextChatContent.builder().text(CHECK_ADDRESS_TEXT).build();
            build.setAction(ChatActionEnum.TEXT.getAction());
            build.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(build));
            return Collections.singletonList(build);
        }

        UserVo userInfo = userService.getUserByTgUserId(tgUserId);

        ZAIBaseChatContent chatContent;
        if (definition != null && StringUtils.equalsIgnoreCase(definition.getAction(), "info")) {
            String ca = definition.getCa();
            CompletableFuture<List<TokenTendencyMaxVo>> kLine = CompletableFuture.supplyAsync(() -> tokenTendencyHandleService.oneDay(ca, Boolean.TRUE), executor);

            SmarterTonBalanceMessage smarterTonBalanceMessage = smartWalletService.buildSendMsg4WebBot(ca);
            String msg = smartWalletService.buildSmartSearchAddress4WebBot(smarterTonBalanceMessage);

            List<TokenTendencyMaxVo> tokenTendencyMaxVos = kLine.join();
            chatContent = ZAITextAndOHLCChatContent.builder().symbol(smarterTonBalanceMessage.getSymbol()).imageUrl(smarterTonBalanceMessage.getImageUrl()).name(smarterTonBalanceMessage.getName()).text(msg).list(tokenTendencyMaxVos).build();
            chatContent.setAction(ChatActionEnum.TOKEN_DETAIL.getAction());
            chatContent.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(chatContent));
            return Collections.singletonList(chatContent);
        } else if (definition != null && StringUtils.equalsIgnoreCase(definition.getAction(), "buy")) {
            String ca = definition.getCa();
            ArrayList<ZAIBaseChatContent> buy = Lists.newArrayList();
            // tokenInfo
            ZAITransferTokenInfoContent tokenInfo = getTokenInfo(ca, tgUserId, userInfo.getAddress());
            tokenInfo.setAction(ChatActionEnum.TOKEN_INFO.getAction());
            tokenInfo.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(tokenInfo));
            buy.add(tokenInfo);

            // check sol
            ZAITransferBalanceCheckContent balanceCheck = checkSolBalance(userInfo.getAddress(), tgUserId, definition.getSolAmount());
            balanceCheck.setAction(ChatActionEnum.BALANCE_CHECK.getAction());
            balanceCheck.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(balanceCheck));
            buy.add(balanceCheck);

            // buy
            if (balanceCheck.getUserBalanceCoin().compareTo(balanceCheck.getNeedCoin()) >= 0) {

                ZShotTransactionSwapVo tsBuy = ZShotTransactionSwapVo.builder().ownerAddress(userInfo.getAddress()).inputMintAddress(TokenAddressConstant.WSOL_ADDRESS).inputMintDecimals(TokenAddressConstant.SOL_DECIMALS).amount(CommonUtils.getAmount(balanceCheck.getNeedCoin(), TokenAddressConstant.SOL_DECIMALS)).outputMintAddress(ca).slippageBps(1000).tgUserId(tgUserId).build();

                ZShotTransactionResponse<ZShotTransactionSignatureResponse> swap = zShotTransactionService.swap(tsBuy);
                String transaction = "";
                if (Objects.nonNull(swap) && Objects.nonNull(swap.getResult()) && StringUtils.isNotEmpty(swap.getResult().getTransaction())) {
                    transaction = swap.getResult().getTransaction();
                }
                if (StringUtils.isEmpty(transaction)) {
                    log.error("buy swap resp ise empty, param is {}", tsBuy);
                }
                ZAITransferBuyTokenContent buyTs = ZAITransferBuyTokenContent.builder().imageUrl(tokenInfo.getImageUrl()).name(tokenInfo.getName()).transferStatus(0).symbol(tokenInfo.getSymbol()).needSol(definition.getSolAmount()).text(transaction).build();
                buyTs.setAction(ChatActionEnum.BUY_TOKEN.getAction());
                buyTs.setOneQuestId(oneQuestId);
                if (Objects.nonNull(swap)) {
                    buyTs.setCode(swap.getCode());
                    buyTs.setMessage(swap.getMessage());
                }
                BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(buyTs));
                buy.add(buyTs);
            }

            return buy;
        } else if (definition != null && StringUtils.equalsIgnoreCase(definition.getAction(), "sell")) {
            String ca = definition.getCa();
            ArrayList<ZAIBaseChatContent> sell = Lists.newArrayList();
            // tokenInfo
            ZAITransferTokenInfoContent tokenInfo = getTokenInfo(ca, tgUserId, userInfo.getAddress());
            tokenInfo.setAction(ChatActionEnum.TOKEN_INFO.getAction());
            tokenInfo.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(tokenInfo));
            sell.add(tokenInfo);


            // check tokens
            ZAITransferBalanceCheckContent balanceCheck = ZAITransferBalanceCheckContent.builder().name(tokenInfo.getName()).symbol(tokenInfo.getSymbol()).imageUrl(tokenInfo.getImageUrl()).needCoin(definition.getAmount()).userBalanceCoin(CommonUtils.getTokens(tokenInfo.getAmount(), tokenInfo.getDecimals())).build();
            balanceCheck.setAction(ChatActionEnum.BALANCE_CHECK.getAction());
            balanceCheck.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(balanceCheck));
            sell.add(balanceCheck);

            if (balanceCheck.getUserBalanceCoin().compareTo(definition.getAmount()) >= 0) {
                ZShotTransactionSwapVo tsSell = ZShotTransactionSwapVo.builder().ownerAddress(userInfo.getAddress()).inputMintAddress(ca).inputMintDecimals(tokenInfo.getDecimals()).amount(CommonUtils.getAmount(definition.getAmount(), tokenInfo.getDecimals())).outputMintAddress(TokenAddressConstant.WSOL_ADDRESS).slippageBps(1000).tgUserId(tgUserId).build();

                ZShotTransactionResponse<ZShotTransactionSignatureResponse> swap = zShotTransactionService.swap(tsSell);
                String transaction = "";
                if (Objects.nonNull(swap) && Objects.nonNull(swap.getResult()) && StringUtils.isNotEmpty(swap.getResult().getTransaction())) {
                    transaction = swap.getResult().getTransaction();
                }
                if (StringUtils.isEmpty(transaction)) {
                    log.error("sell swap resp ise empty, param is {}", tsSell);
                }

                ZAITransferSellTokenContent sellTs = ZAITransferSellTokenContent.builder().imageUrl(tokenInfo.getImageUrl()).name(tokenInfo.getName()).transferStatus(0).symbol(tokenInfo.getSymbol()).tokenAmount(definition.getAmount()).text(transaction).build();
                sellTs.setAction(ChatActionEnum.SELL_TOKEN.getAction());
                sellTs.setOneQuestId(oneQuestId);
                if (Objects.nonNull(swap)) {
                    sellTs.setCode(swap.getCode());
                    sellTs.setMessage(swap.getMessage());
                }
                BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(sellTs));
                sell.add(sellTs);
            }

            return sell;
        } else if (definition != null && StringUtils.equalsIgnoreCase(definition.getAction(), "transfer")) {
            ArrayList<ZAIBaseChatContent> trs = Lists.newArrayList();
            String tokenName = definition.getTokenName().toLowerCase();
            CompletableFuture<Void> preHandle = CompletableFuture.runAsync(() -> userTokenSyncService.syncUserAccount(tgUserId, userInfo.getAddress()), executor);
            userService.sendMessageSyncAccountInfo(tgUserId);
            userTokenSyncService.syncUserTokenListNew(tgUserId, userInfo.getAddress());
            preHandle.join();

            List<UserTokenVo> tokenList = userTokenService.getTokenList(tgUserId, Boolean.TRUE);

            UserTokenVo findInfo = tokenList.stream().filter(s -> StringUtils.containsIgnoreCase(s.getName(), tokenName) || StringUtils.containsIgnoreCase(s.getSymbol(), tokenName)).collect(Collectors.toList()).stream().findFirst().orElse(null);
            log.info("{}findInfo is {}, param is {}", tgUserId, findInfo, definition);
            if (Objects.isNull(findInfo)) {
                ZAITextChatContent build = ZAITextChatContent.builder().text("I couldn't find " + definition.getTokenName() + " in your account").build();
                build.setAction(ChatActionEnum.TEXT.getAction());
                build.setOneQuestId(oneQuestId);
                BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(build));
                return Collections.singletonList(build);
            }
            if (StringUtils.equals(findInfo.getAddress(), TokenAddressConstant.SOL_ADDRESS)) {
                findInfo.setAddress(TokenAddressConstant.WSOL_ADDRESS);
            }
            // tokenInfo
            ZAITransferTokenInfoContent tokenInfo = ZAITransferTokenInfoContent.builder().symbol(findInfo.getSymbol()).amount(findInfo.getAmount()).decimals(findInfo.getDecimals()).imageUrl(findInfo.getImage()).name(findInfo.getName()).text(String.format(HAVE_TOKENS, CommonUtils.getTokens(findInfo.getAmount(), findInfo.getDecimals()), findInfo.getSymbol())).build();
            tokenInfo.setAction(ChatActionEnum.TOKEN_INFO.getAction());
            tokenInfo.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(tokenInfo));
            trs.add(tokenInfo);

            // check tokens
            ZAITransferBalanceCheckContent balanceCheck = ZAITransferBalanceCheckContent.builder().name(findInfo.getName()).symbol(findInfo.getSymbol()).imageUrl(findInfo.getImage()).needCoin(definition.getAmount()).userBalanceCoin(CommonUtils.getTokens(findInfo.getAmount(), findInfo.getDecimals())).build();
            balanceCheck.setAction(ChatActionEnum.BALANCE_CHECK.getAction());
            balanceCheck.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(balanceCheck));
            trs.add(balanceCheck);

            if (balanceCheck.getUserBalanceCoin().compareTo(definition.getAmount()) >= 0) {
                ZShotTransactionTransferVo tsSend = ZShotTransactionTransferVo.builder().fromAddress(userInfo.getAddress()).toAddress(definition.getTargetAddress()).mintAddress(findInfo.getAddress()).amount(CommonUtils.getAmount(definition.getAmount(), findInfo.getDecimals())).decimals(findInfo.getDecimals()).build();
                ZShotTransactionResponse<ZShotTransactionSignatureResponse> transfer = zShotTransactionService.transfer(tsSend);
                String transaction = "";
                if (Objects.nonNull(transfer) && Objects.nonNull(transfer.getResult()) && StringUtils.isNotEmpty(transfer.getResult().getTransaction())) {
                    transaction = transfer.getResult().getTransaction();
                }
                if (StringUtils.isEmpty(transaction)) {
                    log.error("send swap resp is empty, param is {}", tsSend);
                }
                ZAITransferConfirmationContent transferConfirmation = ZAITransferConfirmationContent.builder().symbol(findInfo.getSymbol()).name(findInfo.getName()).transferStatus(0).userAddress(userInfo.getAddress()).targetAccount(definition.getTargetAddress()).userAmount(CommonUtils.getTokens(findInfo.getAmount(), findInfo.getDecimals())).needAmount(definition.getAmount()).text(transaction).build();
                transferConfirmation.setAction(ChatActionEnum.TRANSFER_CONFIRMATION.getAction());
                transferConfirmation.setOneQuestId(oneQuestId);
                if (Objects.nonNull(transfer)) {
                    transferConfirmation.setCode(transfer.getCode());
                    transferConfirmation.setMessage(transfer.getMessage());
                }
                BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(transferConfirmation));
                trs.add(transferConfirmation);
            }

            return trs;
        } else if (definition != null && StringUtils.equalsIgnoreCase(definition.getAction(), "unknown")) {
            ZAITextChatContent build = ZAITextChatContent.builder().text(NOT_SUPPORT).build();
            build.setAction(ChatActionEnum.TEXT.getAction());
            build.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(build));
            return Collections.singletonList(build);

        } else if (definition != null && StringUtils.equalsIgnoreCase(definition.getAction(), "do")) {
            ZAITextChatContent build = ZAITextChatContent.builder().text(DO).build();
            build.setAction(ChatActionEnum.TEXT.getAction());
            build.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(build));
            return Collections.singletonList(build);

        } else {
            chatContent = ZAITextChatContent.builder().text("Not Supported").build();
            chatContent.setAction(ChatActionEnum.TEXT.getAction());
            chatContent.setOneQuestId(oneQuestId);
            return Collections.singletonList(chatContent);
        }
    }


    @Override
    public Boolean sessionLimitedPre(BigInteger tgUserid) {

        String sessionNew = String.format(RedisCacheConstant.LIMIT_SESSION_NEW, LocalDate.now(), tgUserid);
        String sessionNum = String.format(RedisCacheConstant.LIMIT_SESSION_NUM, LocalDate.now(), tgUserid);

        Long sessionNewTemp = redisUtil.get(sessionNew, Long.class);

        if (Objects.nonNull(sessionNewTemp) && sessionNewTemp >= 2) {
            return Boolean.TRUE;
        }

        Long sessionNumTemp = redisUtil.get(sessionNum, Long.class);
        if (Objects.nonNull(sessionNumTemp) && sessionNumTemp >= 100) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean sessionLimitedCheck(BigInteger tgUserid) {

        String sessionNew = String.format(RedisCacheConstant.LIMIT_SESSION_NEW, LocalDate.now(), tgUserid);
        String sessionNum = String.format(RedisCacheConstant.LIMIT_SESSION_NUM, LocalDate.now(), tgUserid);

        Long sessionNewTemp = redisUtil.get(sessionNew, Long.class);

        if (Objects.nonNull(sessionNewTemp) && sessionNewTemp > 2) {
            return Boolean.TRUE;
        }

        Long sessionNumTemp = redisUtil.get(sessionNum, Long.class);
        if (Objects.nonNull(sessionNumTemp) && sessionNumTemp > 100) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void statisticsSession(BigInteger tgUserId, Boolean newSession) {

        String sessionNew = String.format(RedisCacheConstant.LIMIT_SESSION_NEW, LocalDate.now(), tgUserId);
        String sessionNum = String.format(RedisCacheConstant.LIMIT_SESSION_NUM, LocalDate.now(), tgUserId);
        if (BooleanUtils.isTrue(newSession)) {
            redisUtil.increment(sessionNew, 1L, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_1);
        } else {
            redisUtil.increment(sessionNum, 1L, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_1);
        }
    }

    private ZAITransferTokenInfoContent getTokenInfo(String address, BigInteger tgUserId, String myWalletAddress) {

        userService.sendMessageSyncAccountInfo(tgUserId);

        userTokenSyncService.syncUserTokenListNew(tgUserId, myWalletAddress);

        UserTokenVo userTokenVo = userTokenService.queryByAddressAndTgUserId(tgUserId, address);
        if (Objects.nonNull(userTokenVo)) {

            return ZAITransferTokenInfoContent.builder().symbol(userTokenVo.getSymbol()).amount(userTokenVo.getAmount()).decimals(userTokenVo.getDecimals()).imageUrl(userTokenVo.getImage()).name(userTokenVo.getName()).text(String.format(HAVE_TOKENS, CommonUtils.getTokens(userTokenVo.getAmount(), userTokenVo.getDecimals()), userTokenVo.getSymbol())).build();
        }

        TokenDetailVo tokenDetailVo = tokenDetailService.queryCacheWithAsync(address);
        if (Objects.nonNull(tokenDetailVo)) {
            return ZAITransferTokenInfoContent.builder().symbol(tokenDetailVo.getSymbol()).amount(0L).imageUrl(tokenDetailVo.getImage()).name(tokenDetailVo.getName()).text(String.format(NOT_HAVE_TOKENS, tokenDetailVo.getSymbol())).build();
        }

        return new ZAITransferTokenInfoContent();
    }

    private ZAITransferBalanceCheckContent checkSolBalance(String myWalletAddress, BigInteger tgUserId, BigDecimal needSol) {

        userService.sendMessageSyncAccountInfo(tgUserId);

        userTokenSyncService.syncUserAccount(tgUserId, myWalletAddress);

        UserVo userByTgUserId = userService.getUserByTgUserId(tgUserId);

        if (Objects.isNull(userByTgUserId)) {
            return new ZAITransferBalanceCheckContent();
        }

        BigDecimal balanceSol = CommonUtils.getTokens(userByTgUserId.getLamports(), TokenAddressConstant.SOL_DECIMALS);

        return ZAITransferBalanceCheckContent.builder().name("Solana").symbol("Solana").imageUrl("https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/So11111111111111111111111111111111111111112/logo.png").needCoin(needSol).userBalanceCoin(balanceSol).build();
    }

    private ZAITransferBalanceCheckContent checkTokenBalance(String address, BigInteger tgUserId, BigDecimal needSol) {

        userService.sendMessageSyncAccountInfo(tgUserId);

        userTokenSyncService.syncUserTokenListNew(tgUserId, address);

        UserTokenVo userTokenVo = userTokenService.queryByAddressAndTgUserId(tgUserId, address);

        if (Objects.isNull(userTokenVo)) {
            return new ZAITransferBalanceCheckContent();
        }

        BigDecimal balanceCoin = CommonUtils.getTokens(userTokenVo.getAmount(), userTokenVo.getDecimals());

        return ZAITransferBalanceCheckContent.builder().name(userTokenVo.getName()).symbol(userTokenVo.getSymbol()).imageUrl(userTokenVo.getImage()).needCoin(needSol).userBalanceCoin(balanceCoin).build();
    }

}
