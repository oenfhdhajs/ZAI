package cn.z.zai.service.impl;

import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.common.enums.ApiKeyLevelEnum;
import cn.z.zai.service.ApiKeyDetailService;
import cn.z.zai.util.RedisUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ApiKeyDetailServiceImpl implements ApiKeyDetailService {

    @Autowired
    private RedisUtil redisUtil;


    private final Cache<Integer, String> memoryCache = Caffeine.newBuilder()
            .maximumSize(20)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();


    @Override
    public String apiKey(Integer type) {
        String apiKey = memoryCache.getIfPresent(type);
        if (StringUtils.isEmpty(apiKey)) {
            String listKey = String.format(RedisCacheConstant.API_KEY, type, ApiKeyLevelEnum.PRIMARY.getLevel());
            apiKey = redisUtil.leftPopRightPushWithLuaList(listKey);
            if (StringUtils.isEmpty(apiKey)) {
                listKey = String.format(RedisCacheConstant.API_KEY, type, ApiKeyLevelEnum.DEFAULT.getLevel());
                apiKey = redisUtil.leftPopRightPushWithLuaList(listKey);
                if (StringUtils.isEmpty(apiKey)) {
                    log.error("api-key is null :{}", type);
                } else {
                    memoryCache.put(type, apiKey);
                }
                log.warn("api-key primary apiKey is null :{}", type);
            } else {
                memoryCache.put(type, apiKey);
            }
        }
        apiKey = checkApiKeyRequestLimit(type, apiKey);
        return apiKey;
    }

    private String checkApiKeyRequestLimit(Integer type, String apiKey) {
        if (!StringUtils.isEmpty(apiKey)) {
            String newApiKey = null;
            String limitKey = String.format(RedisCacheConstant.API_KEY_REQUEST_LIMIT, type, apiKey);
            if (redisUtil.hasKey(limitKey)) {
                List<ApiKeyLevelEnum> levelEnumList = Arrays.stream(ApiKeyLevelEnum.values()).sorted(((o1, o2) -> o2.getLevel() - o1.getLevel())).collect(Collectors.toList());
                for (ApiKeyLevelEnum apiKeyLevelEnum : levelEnumList) {
                    String listKey = String.format(RedisCacheConstant.API_KEY, type, apiKeyLevelEnum.getLevel());
                    Long size = redisUtil.size4list(listKey);
                    for (int i = 0; i < size; i++) {
                        String newApiKeyTemp = redisUtil.leftPopRightPushWithLuaList(listKey);
                        String checkLimitKey = String.format(RedisCacheConstant.API_KEY_REQUEST_LIMIT, type, newApiKeyTemp);
                        if (!redisUtil.hasKey(checkLimitKey)) {
                            memoryCache.put(type, newApiKeyTemp);
                            newApiKey = newApiKeyTemp;
                            break;
                        }
                    }
                    if (!StringUtils.isEmpty(newApiKey)) {
                        break;
                    }
                }
                log.info("api-key because qpm limit need new apiKey,type={},limitApiKey={},newApiKey={}", type, apiKey, newApiKey);
                if (!StringUtils.isEmpty(newApiKey)) {
                    return newApiKey;
                }
            }
        }
        return apiKey;
    }

}
