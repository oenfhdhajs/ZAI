package cn.z.zai.service.impl;

import cn.z.zai.common.constant.SmartBotConstant;
import cn.z.zai.dto.request.BirdEyePriceRequest;
import cn.z.zai.dto.request.BirdEyeTradeDataSingleRequest;
import cn.z.zai.dto.response.BirdEyePriceResponse;
import cn.z.zai.dto.response.BirdEyeTradeDataMultipleResponseItem;
import cn.z.zai.dto.response.CookieFunContractAddressResp;
import cn.z.zai.dto.vo.SmarterTonBalanceMessage;
import cn.z.zai.dto.vo.TokenDetailVo;
import cn.z.zai.dto.vo.TokenTendencyMaxVo;
import cn.z.zai.service.SmartWalletService;
import cn.z.zai.service.TokenDetailService;
import cn.z.zai.service.TokenSyncService;
import cn.z.zai.service.TokenTendencyHandleService;
import cn.z.zai.util.NumFormat;
import cn.z.zai.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Slf4j
@Service
public class SmartWalletServiceImpl implements SmartWalletService {


    @Autowired
    private TokenDetailService tokenDetailService;

    @Autowired
    private TokenTendencyHandleService tokenTendencyHandleService;

    @Autowired
    private TokenSyncService tokenSyncService;

    @Autowired
    private BirdEyeApi birdEyeApi;

    @Autowired
    private CookieFunApi cookieFunApi;


    @Autowired
    @Qualifier("smartWalletExecutor")
    private Executor executors;


    @Override
    public String buildSmartSearchAddress4WebBot(SmarterTonBalanceMessage smarterTonBalanceMessage) {
        String text = "";


        String price =
                String.format(SmartBotConstant.PRICE, NumFormat.formatBigDecimal(smarterTonBalanceMessage.getPrice()));

        String vol = String.format(SmartBotConstant.VOL, vol(smarterTonBalanceMessage));

        String priceChange =
                String.format(SmartBotConstant.PRICE_CHANGE, priceChangeDesc(smarterTonBalanceMessage.getPriceChange()));

        String mktCap = String.format(SmartBotConstant.MARKET_CAP,
                NumFormat.formatBigDecimal(smarterTonBalanceMessage.getMktCap()));

        String holders =
                String.format(SmartBotConstant.HOLDERS, NumFormat.formatBigInteger(smarterTonBalanceMessage.getHolders()));

        String age = "";
        if (Objects.nonNull(smarterTonBalanceMessage.getDeployTime())
                && smarterTonBalanceMessage.getDeployTime() != 0) {
            age = String.format(SmartBotConstant.AGE, TimeUtil.formatTimeAgo(smarterTonBalanceMessage.getDeployTime()),
                    TimeUtil.epochSeconds2DateTimeStr(smarterTonBalanceMessage.getDeployTime()));
        }

        String ath = "";
        if (Objects.nonNull(smarterTonBalanceMessage.getAllTimeHigh())) {
            ath = String.format(SmartBotConstant.ATH,
                    NumFormat.formatBigDecimal(smarterTonBalanceMessage.getAllTimeHigh()),
                    TimeUtil.formatTimeAgo(smarterTonBalanceMessage.getAllTimeHighTime()));
        }

        String mindShare = "";
        if (Objects.nonNull(smarterTonBalanceMessage.getMindshare())) {
            mindShare = String.format(SmartBotConstant.MIND_SHARE, smarterTonBalanceMessage.getMindshare().setScale(2, RoundingMode.HALF_UP) + "%");
        }

        String followers = "";
        if (Objects.nonNull(smarterTonBalanceMessage.getMindshare())) {
            followers = String.format(SmartBotConstant.FOLLOWERS, smarterTonBalanceMessage.getFollowersCount());
        }

        String smartFollowers = "";
        if (Objects.nonNull(smarterTonBalanceMessage.getMindshare())) {
            smartFollowers = String.format(SmartBotConstant.SMART_FOLLOWERS, smarterTonBalanceMessage.getSmartFollowersCount());
        }

        text = price + mktCap + holders + ath + age + "\n" + priceChange + vol + "\n" + mindShare + followers + smartFollowers;

        return text;
    }


    private String priceChangeDesc(List<BigDecimal> priceChangeList) {

        int size = CollectionUtils.size(priceChangeList);
        for (int i = 0; i < 5 - size; i++) {
            priceChangeList.add(BigDecimal.ZERO);
        }

        if (priceChangeList.get(4).compareTo(BigDecimal.ZERO) != 0) {
            return "1m | 5m | 1h | 12h | 24h: " + priceChangeList.get(0) + "% | " + priceChangeList.get(1) + "% | "
                    + priceChangeList.get(2) + "% | " + priceChangeList.get(3) + "% | " + priceChangeList.get(4) + "% ";
        }

        if (priceChangeList.get(3).compareTo(BigDecimal.ZERO) != 0) {
            return "1m | 5m | 1h | 12h: " + priceChangeList.get(0) + "% | " + priceChangeList.get(1) + "% | "
                    + priceChangeList.get(2) + "% | " + priceChangeList.get(3) + "% ";
        }
        if (priceChangeList.get(2).compareTo(BigDecimal.ZERO) != 0) {
            return "1m | 5m | 1h: " + priceChangeList.get(0) + "% | " + priceChangeList.get(1) + "% | "
                    + priceChangeList.get(2) + "% ";
        }

        if (priceChangeList.get(1).compareTo(BigDecimal.ZERO) != 0) {
            return "1m | 5m: " + priceChangeList.get(0) + "% | " + priceChangeList.get(1) + "% ";
        }

        if (priceChangeList.get(0).compareTo(BigDecimal.ZERO) != 0) {
            return "1m: " + priceChangeList.get(0) + "%";
        }
        return "--";

    }


    private String vol(SmarterTonBalanceMessage smarterTonBalanceMessage) {
        BigDecimal usd_30m = smarterTonBalanceMessage.getVolume_30m_usd();
        BigDecimal usd_1h = smarterTonBalanceMessage.getVolume_1h_usd();
        BigDecimal usd_8h = smarterTonBalanceMessage.getVolume_8h_usd();
        BigDecimal usd_24h = smarterTonBalanceMessage.getVolume_24h_usd();

        if (Objects.nonNull(usd_24h) && usd_24h.compareTo(BigDecimal.ZERO) != 0) {
            return "30m:$" + NumFormat.formatBigDecimal(usd_30m) + " | 1h:$" + NumFormat.formatBigDecimal(usd_1h)
                    + " | 8h:$" + NumFormat.formatBigDecimal(usd_8h) + " | 24h:$" + NumFormat.formatBigDecimal(usd_24h);
        }
        if (Objects.nonNull(usd_8h) && usd_8h.compareTo(BigDecimal.ZERO) != 0) {
            return "30m:$" + NumFormat.formatBigDecimal(usd_30m) + " | 1h:$" + NumFormat.formatBigDecimal(usd_1h)
                    + " | 8h:$" + NumFormat.formatBigDecimal(usd_8h);
        }
        if (Objects.nonNull(usd_1h) && usd_1h.compareTo(BigDecimal.ZERO) != 0) {
            return "30m:$" + NumFormat.formatBigDecimal(usd_30m) + " | 1h:$" + NumFormat.formatBigDecimal(usd_1h);
        }
        return "30m:$" + NumFormat.formatBigDecimal(usd_30m);
    }


    @Override
    public SmarterTonBalanceMessage buildSendMsg4WebBot(String tonAddress) {


        SmarterTonBalanceMessage smarterTonBalanceMessage = new SmarterTonBalanceMessage();
        TokenDetailVo tokenDetailVo = tokenDetailService.queryWithCache(tonAddress);
        if (Objects.isNull(tokenDetailVo)) {
            tokenDetailVo = tokenSyncService.syncTokenByAddressNew(tonAddress);
            if (Objects.isNull(tokenDetailVo)) {
                return smarterTonBalanceMessage;
            }

        }

        // copy  mask first
        dualVolume(tonAddress, smarterTonBalanceMessage);
        baseInfo(tonAddress, smarterTonBalanceMessage);
        priceChange(tonAddress, smarterTonBalanceMessage);
        allTimeHigh(tonAddress, smarterTonBalanceMessage);
        buildCookieFun(tonAddress, smarterTonBalanceMessage);
        return smarterTonBalanceMessage;
    }

    private void baseInfo(String tonAddress, SmarterTonBalanceMessage smarterTonBalanceMessage) {
        TokenDetailVo tokenDetailVo = tokenDetailService.queryWithCache(tonAddress);
        smarterTonBalanceMessage.setSymbol(tokenDetailVo.getSymbol());
        smarterTonBalanceMessage.setName(tokenDetailVo.getName());
        smarterTonBalanceMessage.setAddress(tonAddress);
        smarterTonBalanceMessage.setImageUrl(StringUtils.isEmpty(tokenDetailVo.getOssImage()) ? tokenDetailVo.getImage() : tokenDetailVo.getOssImage());
        smarterTonBalanceMessage.setDescription(tokenDetailVo.getDescription());
        BigDecimal price = tokenDetailService.tokenPriceLast(tonAddress);

        try {
            BirdEyePriceResponse onlinePrice =
                    birdEyeApi.price(BirdEyePriceRequest.builder().address(tonAddress).build());
            if (onlinePrice != null && Objects.nonNull(onlinePrice.getValue())) {
                price = onlinePrice.getValue();
                if (price.scale() > 9) {
                    price = price.setScale(9, RoundingMode.HALF_UP);
                }
            }
        } catch (Exception e) {
            log.warn("SmartWalletService use old price, error msg is {}", e.getMessage());
        }

        smarterTonBalanceMessage.setPrice(Objects.isNull(price) ? BigDecimal.ZERO : price);
        smarterTonBalanceMessage.setMktCap(smarterTonBalanceMessage.getPrice()
                .multiply(Objects.isNull(tokenDetailVo.getTotalSupply()) ? BigDecimal.ZERO : tokenDetailVo.getTotalSupply())
                .setScale(8, RoundingMode.HALF_UP));
        smarterTonBalanceMessage.setDeployTime(Objects.isNull(tokenDetailVo.getDeployTime()) ? 0 : tokenDetailVo.getDeployTime().longValue());
        smarterTonBalanceMessage.setCirculatingSupply(tokenDetailVo.getCirculatingSupply());
        smarterTonBalanceMessage.setHolders(tokenDetailVo.getHolders());
    }

    private void priceChange(String tonAddress, SmarterTonBalanceMessage smarterTonBalanceMessage) {

        BigDecimal price = smarterTonBalanceMessage.getPrice();
        if (Objects.isNull(price)) {
            price = tokenDetailService.tokenPriceLast(tonAddress);
        }
        if (Objects.isNull(price)) {
            price = BigDecimal.ZERO;
        }

        tokenTendencyHandleService.syncHandle(tonAddress);

        List<BigDecimal> priceChange = new ArrayList<>();



        BigDecimal finalPrice = price;
        CompletableFuture<BigDecimal> oneMFuture = CompletableFuture.supplyAsync(() -> {
            List<TokenTendencyMaxVo> oneM = tokenTendencyHandleService.kPriceByDeadMinute(tonAddress, 1);
            if (CollectionUtils.isNotEmpty(oneM)) {
                TokenTendencyMaxVo tokenTendencyMaxVo = oneM.get(0);
                return finalPrice.subtract(tokenTendencyMaxVo.getPrice())
                        .divide(tokenTendencyMaxVo.getPrice(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return BigDecimal.ZERO;
        }, executors);

        BigDecimal finalPrice1 = price;
        CompletableFuture<BigDecimal> fiveMFuture = CompletableFuture.supplyAsync(() -> {
            List<TokenTendencyMaxVo> fiveM = tokenTendencyHandleService.kPriceByDeadMinute(tonAddress, 5);
            if (CollectionUtils.isNotEmpty(fiveM)) {
                TokenTendencyMaxVo tokenTendencyMaxVo = fiveM.get(0);
                return finalPrice1.subtract(tokenTendencyMaxVo.getPrice())
                        .divide(tokenTendencyMaxVo.getPrice(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return BigDecimal.ZERO;
        }, executors);

        BigDecimal finalPrice2 = price;
        CompletableFuture<BigDecimal> oneHFuture = CompletableFuture.supplyAsync(() -> {
            List<TokenTendencyMaxVo> oneH = tokenTendencyHandleService.kPriceByDeadMinute(tonAddress, 60);
            if (CollectionUtils.isNotEmpty(oneH)) {
                TokenTendencyMaxVo tokenTendencyMaxVo = oneH.get(0);
                return finalPrice2.subtract(tokenTendencyMaxVo.getPrice())
                        .divide(tokenTendencyMaxVo.getPrice(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return BigDecimal.ZERO;
        }, executors);

        BigDecimal finalPrice3 = price;
        CompletableFuture<BigDecimal> twelveHFuture = CompletableFuture.supplyAsync(() -> {
            List<TokenTendencyMaxVo> twelveH = tokenTendencyHandleService.kPriceByDeadMinute(tonAddress, 720);
            if (CollectionUtils.isNotEmpty(twelveH)) {
                TokenTendencyMaxVo tokenTendencyMaxVo = twelveH.get(0);
                return finalPrice3.subtract(tokenTendencyMaxVo.getPrice())
                        .divide(tokenTendencyMaxVo.getPrice(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return BigDecimal.ZERO;
        }, executors);

        BigDecimal finalPrice4 = price;
        CompletableFuture<BigDecimal> twentyFourHFuture = CompletableFuture.supplyAsync(() -> {

            List<TokenTendencyMaxVo> twentyFourH = tokenTendencyHandleService.kPriceByDeadMinute(tonAddress, 1440);
            if (CollectionUtils.isNotEmpty(twentyFourH)) {
                TokenTendencyMaxVo tokenTendencyMaxVo = twentyFourH.get(0);
                return finalPrice4.subtract(tokenTendencyMaxVo.getPrice())
                        .divide(tokenTendencyMaxVo.getPrice(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return BigDecimal.ZERO;
        }, executors);

        priceChange.add(oneMFuture.join());

        priceChange.add(fiveMFuture.join());

        priceChange.add(oneHFuture.join());
        priceChange.add(twelveHFuture.join());
        priceChange.add(twentyFourHFuture.join());
        smarterTonBalanceMessage.setPriceChange(priceChange);
    }

    private void allTimeHigh(String tonAddress, SmarterTonBalanceMessage smarterTonBalanceMessage) {
        Long deployTime = smarterTonBalanceMessage.getDeployTime();

        if (Objects.isNull(deployTime) || deployTime == 0) {
            deployTime = TimeUtil.timestampSeconds(LocalDateTime.now().minusDays(1));
        }

        List<TokenTendencyMaxVo> tokenTendencyMaxVos =
                tokenTendencyHandleService.todayTendencyAllDetail(tonAddress, deployTime, Boolean.FALSE);
        TokenTendencyMaxVo max = null;
        if (CollectionUtils.isNotEmpty(tokenTendencyMaxVos)) {
            max = Collections.max(tokenTendencyMaxVos, Comparator.comparing(TokenTendencyMaxVo::getPrice));
        }

        BigDecimal circulatingSupply = Objects.isNull(smarterTonBalanceMessage.getCirculatingSupply()) ? BigDecimal.ZERO
                : smarterTonBalanceMessage.getCirculatingSupply();
        if (Objects.isNull(max)) {
            smarterTonBalanceMessage.setAllTimeHigh(smarterTonBalanceMessage.getPrice().multiply(circulatingSupply));
            smarterTonBalanceMessage.setAllTimeHighTime(TimeUtil.currentEpochSecond());
        } else {
            smarterTonBalanceMessage.setAllTimeHigh(max.getPrice().multiply(circulatingSupply));
            smarterTonBalanceMessage.setAllTimeHighTime(max.getUnixTime());
        }

    }

    private void buildCookieFun(String tonAddress, SmarterTonBalanceMessage smarterTonBalanceMessage) {
        CookieFunContractAddressResp cookieFunContractAddressResp = cookieFunApi.cookieFunContractAddress(tonAddress);

        if (Objects.isNull(cookieFunContractAddressResp)) {
            return;
        }
        if (BooleanUtils.isFalse(cookieFunContractAddressResp.getSuccess())) {
            return;
        }
        if (Objects.isNull(cookieFunContractAddressResp.getOk())) {
            return;
        }
        smarterTonBalanceMessage.setMindshare(cookieFunContractAddressResp.getOk().getMindshare());
        smarterTonBalanceMessage.setFollowersCount(cookieFunContractAddressResp.getOk().getFollowersCount());
        smarterTonBalanceMessage.setSmartFollowersCount(cookieFunContractAddressResp.getOk().getSmartFollowersCount());
    }


    private void dualVolume(String tonAddress, SmarterTonBalanceMessage smarterTonBalanceMessage) {
        BirdEyeTradeDataSingleRequest quest = new BirdEyeTradeDataSingleRequest();
        quest.setAddress(tonAddress);
        BirdEyeTradeDataMultipleResponseItem resp = birdEyeApi.tradeDataSingle(quest);

        if (Objects.isNull(resp)) {
            return;
        }
        BeanUtils.copyProperties(resp, smarterTonBalanceMessage);
    }


}
