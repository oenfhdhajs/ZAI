package cn.z.zai.service;

import cn.z.zai.dto.vo.TokenDetailVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TokenDetailService {

    Map<String, BigDecimal> tokenPriceLast(List<String> addressList);

    BigDecimal tokenPriceLast(String address);

    Boolean saveTokenDetail(TokenDetailVo vo);

    TokenDetailVo queryWithCache(String address);


    Integer addTokenDetail(TokenDetailVo vo);

    void updateTokenDetail(TokenDetailVo vo);

    void sendMsg4UpdateTokenDetailLastShowDetailTime(String address);


    TokenDetailVo queryCacheWithAsync(String address);
}
