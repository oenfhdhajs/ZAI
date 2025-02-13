package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyeTradeDataMultipleResponseItem {


    private BigInteger holder;

    private Integer market;

    private Long last_trade_unix_time;

    private LocalDateTime last_trade_human_time;

    private BigDecimal price;

    private BigDecimal history_30m_price;

    private BigDecimal price_change_30m_percent;

    private BigDecimal history_1h_price;

    private BigDecimal price_change_1h_percent;

    private BigDecimal history_2h_price;

    private BigDecimal price_change_2h_percent;

    private BigDecimal history_4h_price;

    private BigDecimal price_change_4h_percent;

    private BigDecimal history_6h_price;

    private BigDecimal price_change_6h_percent;

    private BigDecimal history_8h_price;

    private BigDecimal price_change_8h_percent;

    private BigDecimal history_12h_price;

    private BigDecimal price_change_12h_percent;

    private BigDecimal history_24h_price;

    private BigDecimal price_change_24h_percent;

    private BigDecimal unique_wallet_30m;

    private BigDecimal unique_wallet_history_30m;

    private BigDecimal unique_wallet_30m_change_percent;

    private BigDecimal unique_wallet_1h;

    private BigDecimal unique_wallet_history_1h;

    private BigDecimal unique_wallet_1h_change_percent;

    private BigDecimal unique_wallet_2h;

    private BigDecimal unique_wallet_history_2h;

    private BigDecimal unique_wallet_2h_change_percent;

    private BigDecimal unique_wallet_4h;

    private BigDecimal unique_wallet_history_4h;

    private BigDecimal unique_wallet_4h_change_percent;

    private BigDecimal unique_wallet_8h;

    private BigDecimal unique_wallet_history_8h;

    private BigDecimal unique_wallet_8h_change_percent;

    private BigDecimal unique_wallet_24h;

    private BigDecimal unique_wallet_history_24h;

    private BigDecimal unique_wallet_24h_change_percent;

    private BigDecimal trade_30m;

    private BigDecimal trade_history_30m;

    private BigDecimal trade_30m_change_percent;

    private BigDecimal sell_30m;

    private BigDecimal sell_history_30m;

    private BigDecimal sell_30m_change_percent;

    private BigDecimal buy_30m;

    private BigDecimal buy_history_30m;

    private BigDecimal buy_30m_change_percent;

    private BigDecimal volume_30m;

    private BigDecimal volume_30m_usd;

    private BigDecimal volume_history_30m;

    private BigDecimal volume_history_30m_usd;

    private BigDecimal volume_buy_30m_usd;

    private BigDecimal volume_sell_30m_usd;

    //=========volume
    private BigDecimal volume_1h;

    private BigDecimal volume_1h_usd;

    private BigDecimal volume_buy_1h;

    private BigDecimal volume_buy_1h_usd;

    private BigDecimal volume_sell_1h;

    private BigDecimal volume_sell_1h_usd;

    /**
     * volume_buy_1h_usd - volume_sell_1h_usd
     */
    private BigDecimal netPurchase_1h_usd;

    private BigDecimal trade_1h;


    private BigDecimal volume_8h;

    private BigDecimal volume_8h_usd;

    private BigDecimal volume_buy_8h;

    private BigDecimal volume_buy_8h_usd;

    private BigDecimal volume_sell_8h;

    private BigDecimal volume_sell_8h_usd;

    /**
     * volume_buy_8h_usd - volume_sell_8h_usd
     */
    private BigDecimal netPurchase_8h_usd;

    private BigDecimal trade_8h;


    private BigDecimal volume_24h;

    private BigDecimal volume_24h_usd;

    private BigDecimal volume_buy_24h;

    private BigDecimal volume_buy_24h_usd;

    private BigDecimal volume_sell_24h;

    private BigDecimal volume_sell_24h_usd;

    /**
     * volume_buy_24h_usd - volume_sell_24h_usd
     */
    private BigDecimal netPurchase_24h_usd;

    private BigDecimal volume_history_24h;

    private BigDecimal volume_history_24h_usd;

    private BigDecimal volume_24h_change_percent;

    private BigDecimal trade_24h;


    /**
     *       "volume_30m_change_percent": 160.1541800337,
     *       "volume_buy_30m": 99326.374881613,
     *       "volume_buy_30m_usd": 345346.45722971,
     *       "volume_buy_history_30m": 24900.581085243,
     *       "volume_buy_history_30m_usd": 86788.62224907,
     *       "volume_buy_30m_change_percent": 298.89179510143,
     *       "volume_sell_30m": 38384.918698152,
     *       "volume_sell_30m_usd": 131554.27923462,
     *       "volume_sell_history_30m": 28033.910900212,
     *       "volume_sell_history_30m_usd": 95116.828818043,
     *       "volume_sell_30m_change_percent": 36.923167212685,
     *       "trade_history_1h": 2453,
     *       "trade_1h_change_percent": -59.804321239299,
     *       "sell_1h": 460,
     *       "sell_history_1h": 1208,
     *       "sell_1h_change_percent": -61.920529801325,
     *       "buy_1h": 526,
     *       "buy_history_1h": 1245,
     *       "buy_1h_change_percent": -57.751004016064,
     *
     *       "volume_history_1h": 682088.65042648,
     *       "volume_history_1h_usd": 2330963.8169066,
     *       "volume_1h_change_percent": -72.049705643686,
     *       "volume_buy_history_1h": 378357.57611631,
     *       "volume_buy_history_1h_usd": 1295372.6223075,
     *       "volume_buy_1h_change_percent": -67.166785123747,
     *       "volume_sell_history_1h": 303731.07431017,
     *       "volume_sell_history_1h_usd": 1035591.1945991,
     *       "volume_sell_1h_change_percent": -78.132356147881,
     *       "trade_2h": 3439,
     *       "trade_history_2h": 1158,
     *       "trade_2h_change_percent": 196.97754749568,
     *       "sell_2h": 1668,
     *       "sell_history_2h": 505,
     *       "sell_2h_change_percent": 230.29702970297,
     *       "buy_2h": 1771,
     *       "buy_history_2h": 653,
     *       "buy_2h_change_percent": 171.20980091884,
     *       "volume_2h": 872734.4359917,
     *       "volume_2h_usd": 2978547.6926378,
     *       "volume_history_2h": 120029.99074224,
     *       "volume_history_2h_usd": 387493.22632696,
     *       "volume_2h_change_percent": 627.09697850921,
     *       "volume_buy_2h": 502584.53208316,
     *       "volume_buy_2h_usd": 1711833.5188246,
     *       "volume_buy_history_2h": 74674.81923179,
     *       "volume_buy_history_2h_usd": 235881.83800754,
     *       "volume_buy_2h_change_percent": 573.03079840494,
     *       "volume_sell_2h": 370149.90390853,
     *       "volume_sell_2h_usd": 1266714.1738132,
     *       "volume_sell_history_2h": 45355.171510447,
     *       "volume_sell_history_2h_usd": 151611.38831942,
     *       "volume_sell_2h_change_percent": 716.11399887061,
     *       "trade_4h": 4597,
     *       "trade_history_4h": 2648,
     *       "trade_4h_change_percent": 73.602719033233,
     *       "sell_4h": 2173,
     *       "sell_history_4h": 1683,
     *       "sell_4h_change_percent": 29.1146761735,
     *       "buy_4h": 2424,
     *       "buy_history_4h": 965,
     *       "buy_4h_change_percent": 151.19170984456,
     *       "volume_4h": 992761.51783429,
     *       "volume_4h_usd": 3372982.5188389,
     *       "volume_history_4h": 435719.02527529,
     *       "volume_history_4h_usd": 1446464.6622382,
     *       "volume_4h_change_percent": 127.84442731346,
     *       "volume_buy_4h": 577259.35131496,
     *       "volume_buy_4h_usd": 1962129.9023482,
     *       "volume_buy_history_4h": 190392.57588403,
     *       "volume_buy_history_4h_usd": 630914.72038987,
     *       "volume_buy_4h_change_percent": 203.19425462607,
     *       "volume_sell_4h": 415502.16651933,
     *       "volume_sell_4h_usd": 1410852.6164907,
     *       "volume_sell_history_4h": 245326.44939127,
     *       "volume_sell_history_4h_usd": 815549.94184838,
     *       "volume_sell_4h_change_percent": 69.367048498163,
     *       "trade_history_8h": 6541,
     *       "trade_8h_change_percent": 10.762880293533,
     *       "sell_8h": 3856,
     *       "sell_history_8h": 3465,
     *       "sell_8h_change_percent": 11.284271284271,
     *       "buy_8h": 3389,
     *       "buy_history_8h": 3076,
     *       "buy_8h_change_percent": 10.1755526658,
     *       "volume_8h": 1428480.5431096,
     *       "volume_8h_usd": 4829210.6007109,
     *       "volume_history_8h": 1119115.2692986,
     *       "volume_history_8h_usd": 3682172.2111906,
     *       "volume_8h_change_percent": 27.643736288656,
     *       "volume_buy_8h": 767651.92719897,
     *       "volume_buy_8h_usd": 2600490.9765693,
     *       "volume_buy_history_8h": 562947.30485906,
     *       "volume_buy_history_8h_usd": 1854900.8323914,
     *       "volume_buy_8h_change_percent": 36.363016675454,
     *       "volume_sell_8h": 660828.61591058,
     *       "volume_sell_8h_usd": 2228719.6241416,
     *       "volume_sell_history_8h": 556167.96443952,
     *       "volume_sell_history_8h_usd": 1827271.3787992,
     *       "volume_sell_8h_change_percent": 18.81817331506,
     *       "trade_history_24h": 17252,
     *       "trade_24h_change_percent": 20.959888708556,
     *       "sell_24h": 10999,
     *       "sell_history_24h": 7615,
     *       "sell_24h_change_percent": 44.438608010506,
     *       "buy_24h": 9869,
     *       "buy_history_24h": 9637,
     *       "buy_24h_change_percent": 2.4073881913459,
     *       "volume_buy_24h": 2141494.3337163,
     *       "volume_buy_24h_usd": 7013945.1943333,
     *       "volume_buy_history_24h": 2010936.9411844,
     *       "volume_buy_history_24h_usd": 6406879.2372631,
     *       "volume_buy_24h_change_percent": 6.4923663123437,
     *       "volume_sell_24h": 2050229.1938061,
     *       "volume_sell_24h_usd": 6668084.029396,
     *       "volume_sell_history_24h": 2312850.2675053,
     *       "volume_sell_history_24h_usd": 7390332.3189588,
     *       "volume_sell_24h_change_percent": -11.354867082792
     *
     */

    public BigDecimal getNetPurchase_1h_usd() {
        return (getVolume_buy_1h_usd() != null ? getVolume_buy_1h_usd() : BigDecimal.ZERO ).subtract(getVolume_sell_1h_usd() != null ? getVolume_sell_1h_usd() : BigDecimal.ZERO );
    }

    public BigDecimal getNetPurchase_8h_usd() {
        return (getVolume_buy_8h_usd() != null ? getVolume_buy_8h_usd() : BigDecimal.ZERO ).subtract(getVolume_sell_8h_usd() != null ? getVolume_sell_8h_usd() : BigDecimal.ZERO );
    }

    public BigDecimal getNetPurchase_24h_usd() {
        return (getVolume_buy_24h_usd() != null ? getVolume_buy_24h_usd() : BigDecimal.ZERO ).subtract(getVolume_sell_24h_usd() != null ? getVolume_sell_24h_usd() : BigDecimal.ZERO );
    }
}
