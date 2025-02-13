package cn.z.zai.config.ratelimiter;

import cn.z.zai.exception.ServiceException;
import cn.z.zai.common.constant.ErrorConstant;
import cn.z.zai.common.enums.ResponseCodeEnum;
import cn.z.zai.util.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;


@Slf4j
@Aspect
@Component
public class RateLimiterAOP implements InitializingBean {

    @Value("${spring.profiles.active}")
    private String env;


    @Value(value = "${spring.application.name}")
    private String serviceName;

    private static String PRE_KEY;

    @Resource
    private RedissonClient redissonClient;


    @Pointcut("@annotation(cn.z.zai.config.ratelimiter.AccessInterceptor)")
    public void aopPoint() {
    }


    @Around("aopPoint() && @annotation(accessInterceptor)")
    public Object doRouter(ProceedingJoinPoint jp, AccessInterceptor accessInterceptor) throws Throwable {
        String key = accessInterceptor.key();
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("RateLimiter key is null or empty!");
        }
        String keyAttr = key;
        if (StringUtils.equals("user", key)) {
            keyAttr = ContextHolder.getUserId().toString();
        }

        // limit
        if (!isRateLimited(jp, keyAttr, accessInterceptor)) {
            log.error("Request rate limited: {}", keyAttr);
            throw new ServiceException(new ResponseCodeEnum(ErrorConstant.IGNORE_ERROR, accessInterceptor.desc()));
            //return fallbackMethodResult(jp, accessInterceptor.fallbackMethod());
        }

        // resp
        return jp.proceed();
    }

    /**
     * black
     *
     * @param keyAttr
     * @param accessInterceptor
     * @return
     */
    private boolean isBlacklisted(ProceedingJoinPoint jp, String keyAttr, AccessInterceptor accessInterceptor) {

        String blacklistKey = PRE_KEY + ":blacklist:" + LocalDate.now() + keyAttr;
        long count = redissonClient.getAtomicLong(blacklistKey).incrementAndGet();
        redissonClient.getAtomicLong(blacklistKey).expire(accessInterceptor.blacklistDurationSeconds(), TimeUnit.SECONDS);
        return count > accessInterceptor.blacklistCount();
    }

    /**
     * rest limit
     *
     * @param keyAttr
     * @param accessInterceptor
     * @return
     */
    private boolean isRateLimited(ProceedingJoinPoint jp, String keyAttr, AccessInterceptor accessInterceptor) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        String methodName = signature.getMethod().getName();
        String rateLimitKey = PRE_KEY + ":rateLimit:" + methodName + ":" + keyAttr;
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateLimitKey);
        rateLimiter.trySetRate(RateType.OVERALL, (long) accessInterceptor.permitsPerSecond(), 1, RateIntervalUnit.SECONDS);
        rateLimiter.expire(24 * 3600, TimeUnit.SECONDS);
        return rateLimiter.tryAcquire();
    }


    @Override
    public void afterPropertiesSet() {

        PRE_KEY = env + ":" + serviceName;

    }
}
