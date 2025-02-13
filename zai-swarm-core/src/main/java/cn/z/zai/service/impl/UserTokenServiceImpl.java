package cn.z.zai.service.impl;

import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.common.constant.TokenAddressConstant;
import cn.z.zai.dao.UserTokenDao;
import cn.z.zai.dto.entity.UserToken;
import cn.z.zai.dto.vo.UserTokenVo;
import cn.z.zai.dto.vo.UserVo;
import cn.z.zai.service.TokenDetailService;
import cn.z.zai.service.UserService;
import cn.z.zai.service.UserTokenService;
import cn.z.zai.util.RedisUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private UserTokenDao userTokenDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;


    @Autowired
    @Lazy
    private TokenDetailService tokenDetailService;


    @Override
    public UserTokenVo queryByAddressAndTgUserId(BigInteger tgUserId, String address) {
        List<UserTokenVo> tokenList = getTokenList(tgUserId, false);
        UserTokenVo userTokenVo = null;
        if (!CollectionUtils.isEmpty(tokenList)) {
            Optional<UserTokenVo> first = tokenList.stream().filter(e -> e.getAddress().equals(address)).findFirst();
            if (first.isPresent()) {
                userTokenVo = first.get();
            }
        }

        return userTokenVo;
    }

    @Override
    public List<UserTokenVo> getTokenList(BigInteger tgUserId, Boolean containSol) {
        List<UserTokenVo> userTokenList = redisUtil.get(String.format(RedisCacheConstant.USER_TOKEN_LIST, tgUserId), new TypeReference<List<UserTokenVo>>() {
        });
        if (userTokenList == null) {
            userTokenList = userTokenDao.getTokenList(tgUserId);
            if (userTokenList.size() > 0) {
                redisUtil.set(String.format(RedisCacheConstant.USER_TOKEN_LIST, tgUserId), userTokenList, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_1);
            }
        }
        if (userTokenList.size() > 0) {
            List<String> addressList = userTokenList.stream().map(UserTokenVo::getAddress).collect(Collectors.toList());
            Map<String, BigDecimal> tokenPriceMap = tokenDetailService.tokenPriceLast(addressList);
            userTokenList.forEach(item -> {
                BigDecimal price = tokenPriceMap.get(item.getAddress());
                if (price != null) {
                    item.setPrice(price);
                }
            });
        }
        if (containSol) {
            UserVo userVo = userService.getUserByTgUserId(tgUserId);
            if (Objects.nonNull(userVo.getLamports()) && userVo.getLamports() > 0) {
                BigDecimal value = tokenDetailService.tokenPriceLast(TokenAddressConstant.WSOL_ADDRESS);
                UserTokenVo tokenVo = new UserTokenVo();
                tokenVo.setAddress(TokenAddressConstant.SOL_ADDRESS);
                tokenVo.setDecimals(TokenAddressConstant.SOL_DECIMALS);
                tokenVo.setName(TokenAddressConstant.SOL_NAME);
                tokenVo.setSymbol(TokenAddressConstant.SOL_SYMBOL);
                tokenVo.setImage(TokenAddressConstant.SOL_IMAGE);
                tokenVo.setAmount(userVo.getLamports());
                tokenVo.setPrice(value);
                userTokenList.add(tokenVo);
            }
        }


        return userTokenList;
    }


    @Override
    @Transactional
    public void insertTokenList(List<UserToken> tokenList, BigInteger tgUserId) {

        List<UserTokenVo> oldTokenList = getTokenList(tgUserId, false);
        boolean same = areListsEqual(oldTokenList, tokenList);
        if (!same) {
            String key = String.format(RedisCacheConstant.USER_TOKEN_LIST_UPDATE_KEY, tgUserId);
            if (!redisUtil.hasKey(key)) {
                try {
                    userTokenDao.deleteTokenList(tgUserId);
                    if (tokenList.size() > 0) {
                        List<UserToken> filteredList = tokenList.stream()
                                .filter(token -> token.getAmount() != 0)
                                .collect(Collectors.toList());
                        if (filteredList.size() > 0) {
                            userTokenDao.insertTokenList(filteredList);
                        }
                    }
                    redisUtil.delete(String.format(RedisCacheConstant.USER_TOKEN_LIST, tgUserId));
                } catch (Exception exception) {
                    log.error("insertTokenList exception {}", tgUserId, exception);
                }
            }

        }
    }

    public static boolean areListsEqual(List<UserTokenVo> list1, List<UserToken> list2) {
        List<UserToken> convertList1 = list1.stream().map(vo -> {
            UserToken token = new UserToken();
            token.setAddress(vo.getAddress());
            token.setAmount(vo.getAmount());
            return token;
        }).collect(Collectors.toList());

        Function<UserToken, List<Object>> compositeKey = token ->
                Arrays.asList(token.getAddress(), token.getAmount());

        Set<List<Object>> set1 = convertList1.stream()
                .map(compositeKey)
                .collect(Collectors.toSet());
        Set<List<Object>> set2 = list2.stream()
                .map(compositeKey)
                .collect(Collectors.toSet());

        return set1.equals(set2);
    }
}
