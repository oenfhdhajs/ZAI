package cn.z.zai.util;

import cn.z.zai.dto.response.BirdEyeOHLCVResponse;
import cn.z.zai.dto.response.BirdEyeOHLCVResponseItem;
import cn.z.zai.dto.response.BirdEyeTokenOverviewResponse;
import cn.z.zai.dto.vo.TokenDetailVo;
import cn.z.zai.dto.vo.TokenTendencyMaxVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Slf4j
public class TokenUtils {


    public static void packageTokenDetailVoByOverview(TokenDetailVo vo, BirdEyeTokenOverviewResponse response) {
        if (vo != null && response != null) {
            // base info
            if (StringUtils.isEmpty(vo.getName())) {
                vo.setName(response.getName());
            }
            if (StringUtils.isEmpty(vo.getAddress())) {
                vo.setAddress(response.getAddress());
            }
            if (vo.getDecimals() == null) {
                vo.setDecimals(response.getDecimals());
            }
            if (StringUtils.isEmpty(vo.getImage())) {
                vo.setImage(response.getLogoURI());
            }
            if (StringUtils.isEmpty(vo.getSymbol())) {
                vo.setSymbol(response.getSymbol());
            }
            if (vo.getTotalSupply() == null) {
                vo.setTotalSupply(response.getSupply());
            }
            if (response.getExtensions() != null) {
                vo.setDescription(response.getExtensions().getDescription());
                if (response.getExtensions().getTwitter() != null) {
                    int index = response.getExtensions().getTwitter().lastIndexOf("/");
                    index = index == 0 ? index : index + 1;
                    vo.setTwitterScreenName(response.getExtensions().getTwitter().substring(index));
                }
            }
            //market info
            if (vo.getMktCap() == null) {
                vo.setMktCap(response.getMc());
            }
            if (vo.getHolders() == null) {
                vo.setHolders(response.getHolder());
            }
            if (vo.getPrice() == null) {
                vo.setPrice(response.getPrice());
            }
            if (vo.getCirculatingSupply() == null) {
                vo.setCirculatingSupply(response.getCirculatingSupply());
            }
            if (vo.getVolumePast24h() == null) {
                vo.setVolumePast24h(response.getV24hUSD());
            }
            if (vo.getPrice24hChange() == null) {
                vo.setPrice24hChange(response.getPriceChange24hPercent());
            }
            //other
            vo.setLastDateTime(TimeUtil.currentLocalDateTime());
            vo.setLastTimestamp(BigInteger.valueOf(TimeUtil.localDateTimeToMills(vo.getLastDateTime())));
        }
    }


    public static List<TokenTendencyMaxVo> buildByTokenOHLCResponse(BirdEyeOHLCVResponse response) {
        if (!CollectionUtils.isEmpty(response.getItems())) {
            List<TokenTendencyMaxVo> list = Lists.newArrayList();
            for (BirdEyeOHLCVResponseItem item : response.getItems()) {
                TokenTendencyMaxVo vo = new TokenTendencyMaxVo();
                vo.setAddress(item.getAddress());
                vo.setOpenPrice(item.getO());
                vo.setHighPrice(item.getH());
                vo.setLowPrice(item.getL());
                vo.setClosePrice(item.getC());
                vo.setLastTimestamp(BigInteger.valueOf(item.getUnixTime()).multiply(BigInteger.valueOf(1000)));
                vo.setLastDateTime(TimeUtil.epochMilli2DateTime(vo.getLastTimestamp().longValue()));
                vo.setCurrentDay(vo.getLastDateTime().toLocalDate());
                BigDecimal averagePrice = vo.getOpenPrice().add(vo.getHighPrice()).add(vo.getLowPrice()).add(vo.getClosePrice()).divide(BigDecimal.valueOf(4), 9, RoundingMode.HALF_UP);
                vo.setPrice(averagePrice);
                vo.setUnixTime(item.getUnixTime());
                list.add(vo);
            }
            list.sort(Comparator.comparing(TokenTendencyMaxVo::getLastTimestamp));
            return list;
        }
        return Collections.emptyList();
    }


}
