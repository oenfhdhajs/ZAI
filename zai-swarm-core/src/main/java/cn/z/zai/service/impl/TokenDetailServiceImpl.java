package cn.z.zai.service.impl;

import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.dao.TokenDetailDao;
import cn.z.zai.dto.vo.TokenDetailVo;
import cn.z.zai.service.TokenDetailService;
import cn.z.zai.service.TokenSecurityDetailService;
import cn.z.zai.service.TokenSyncService;
import cn.z.zai.util.RedisUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenDetailServiceImpl implements TokenDetailService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TokenDetailDao dao;


    @Autowired
    @Lazy
    private TokenSyncService tokenSyncService;


    @Autowired
    @Lazy
    private TokenDetailService self;

    @Autowired
    private TokenSecurityDetailService tokenSecurityDetailService;


    @Override
    public Map<String, BigDecimal> tokenPriceLast(List<String> addressList) {
        Map<String, BigDecimal> map = Maps.newHashMap();
        for (String address : addressList) {
            BigDecimal price = tokenPriceLast(address);
            if (price != null) {
                map.put(address, price);
            }
        }
        return map;
    }

    @Override
    public BigDecimal tokenPriceLast(String address) {
        try {
            self.sendMsg4UpdateTokenDetailLastShowDetailTime(address);
            String cacheKey = String.format(RedisCacheConstant.BIRD_EYE_PRICE_MULTIPLE_KEY, address);
            BigDecimal price = redisUtil.get(cacheKey, BigDecimal.class);
            if (price == null) {
                TokenDetailVo vo = queryWithCache(address);
                if (vo == null) {
                    vo = tokenSyncService.syncTokenByAddressNew(address);
                }
                if (vo != null) {
                    price = vo.getPrice();
                }
            }
            return price;
        } catch (Exception e) {
            log.error("Method [tokenPriceLast] ERROR: {}", address, e);
        }
        return null;
    }


    private void packagePrice(List<? extends TokenDetailVo> cacheList) {
        if (!CollectionUtils.isEmpty(cacheList)) {
            for (TokenDetailVo vo : cacheList) {
                String cacheKey = String.format(RedisCacheConstant.BIRD_EYE_PRICE_MULTIPLE_KEY, vo.getAddress());
                BigDecimal price = redisUtil.get(cacheKey, BigDecimal.class);
                if (price != null) {
                    vo.setPrice(price);
                    if (vo.getTotalSupply() != null && vo.getTotalSupply().compareTo(BigDecimal.ZERO) > 0) {
                        vo.setMktCap(vo.getPrice().multiply(vo.getTotalSupply()));
                    }
                }
                self.sendMsg4UpdateTokenDetailLastShowDetailTime(vo.getAddress());

            }
        }
    }


    @Override
    public Boolean saveTokenDetail(TokenDetailVo vo) {
        processValueDecimal(Lists.newArrayList(vo));
        TokenDetailVo tokenDetailVo = queryWithCache(vo.getAddress());

        if (tokenDetailVo == null) {
            addTokenDetail(vo);
            try {
                tokenSecurityDetailService.insertTokenSecurity(vo.getAddress());
            } catch (Exception e) {
                log.error("cn.z.shot.service.TokenSecurityDetailService.insertTokenSecurity ERROR :", e);
            }
            String key = String.format(RedisCacheConstant.TOKEN_DETAIL_KEY, vo.getAddress());
            redisUtil.set(key, vo, RedisCacheConstant.TOKEN_DETAIL_KEY_TIMEOUT);
        } else {

            vo.setManualEditor(tokenDetailVo.getManualEditor());
            vo.setId(tokenDetailVo.getId());
            updateTokenDetail(vo);
        }
        return Boolean.TRUE;
    }

    private void processValueDecimal(List<TokenDetailVo> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        for (TokenDetailVo vo : voList) {
            if (vo.getPrice() != null && vo.getPrice().scale() > 9) {
                vo.setPrice(vo.getPrice().setScale(9, RoundingMode.HALF_UP));
            }
            if (vo.getPrice24hChange() != null && vo.getPrice24hChange().scale() > 9) {
                vo.setPrice24hChange(vo.getPrice24hChange().setScale(9, RoundingMode.HALF_UP));
            }
            if (vo.getMktCap() != null && vo.getMktCap().scale() > 9) {
                vo.setMktCap(vo.getMktCap().setScale(9, RoundingMode.HALF_UP));
            }
            if (vo.getTotalSupply() != null && vo.getTotalSupply().scale() > 9) {
                vo.setTotalSupply(vo.getTotalSupply().setScale(9, RoundingMode.HALF_UP));
            }
            if (vo.getVolumePast24h() != null && vo.getVolumePast24h().scale() > 4) {
                vo.setVolumePast24h(vo.getVolumePast24h().setScale(4, RoundingMode.HALF_UP));
            }
            if (vo.getCirculatingSupply() != null && vo.getCirculatingSupply().scale() > 9) {
                vo.setCirculatingSupply(vo.getCirculatingSupply().setScale(9, RoundingMode.HALF_UP));
            }
        }
    }

    @Override
    public TokenDetailVo queryWithCache(String address) {
        String key = String.format(RedisCacheConstant.TOKEN_DETAIL_KEY, address);
        TokenDetailVo vo = redisUtil.get(key, TokenDetailVo.class);
        if (vo == null) {
            vo = dao.queryByAddress(address);
            if (vo != null) {
                redisUtil.set(key, vo, RedisCacheConstant.TOKEN_DETAIL_KEY_TIMEOUT);
            }
        }
        if (vo != null) {
            packagePrice(Lists.newArrayList(vo));
        }
        return vo;
    }


    @Override
    public Integer addTokenDetail(TokenDetailVo vo) {
        Integer id = null;
        RLock lock = null;
        try {
            lock = redisUtil.lock("addTokenDetail_" + vo.getAddress());
            boolean b = lock.tryLock(3, 2, TimeUnit.SECONDS);
            if (b) {
                TokenDetailVo tokenDetailVo = queryWithCache(vo.getAddress());
                if (tokenDetailVo == null) {
                    processValueDecimal(Lists.newArrayList(vo));
                    dao.addTokenDetail(vo);
                    id = vo.getId();
                } else {
                    id = tokenDetailVo.getId();
                }
            }
        } catch (Exception e) {
            log.error("addTokenDetail-lock ERROR:", e);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return id;
    }

    @Override
    public void updateTokenDetail(TokenDetailVo vo) {
        processValueDecimal(Lists.newArrayList(vo));
        dao.updateTokenDetail(vo);
        String key = String.format(RedisCacheConstant.TOKEN_DETAIL_KEY, vo.getAddress());
        redisUtil.delete(key);
    }

    @Override
    public void sendMsg4UpdateTokenDetailLastShowDetailTime(String address) {
        if (!redisUtil.setIfAbsent("showDetailTime" + address, "1", 600, TimeUnit.SECONDS)) {
            return;
        }

    }


    @Override
    public TokenDetailVo queryCacheWithAsync(String address) {
        TokenDetailVo tokenDetailVo = queryWithCache(address);
        if (Objects.isNull(tokenDetailVo)) {
            tokenDetailVo = tokenSyncService.syncTokenByAddressNew(address);
            if (Objects.nonNull(tokenDetailVo)) {
                return tokenDetailVo;
            }

        }
        return tokenDetailVo;
    }

}
