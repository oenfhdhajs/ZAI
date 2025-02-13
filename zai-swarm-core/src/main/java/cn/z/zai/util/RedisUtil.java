package cn.z.zai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil implements InitializingBean {
    @Value("${spring.profiles.active}")
    private String env;


    @Value(value = "${spring.application.name}")
    private String serviceName;
    @Autowired
    private JsonUtil jsonUtil;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisObjTemplate;
    @Autowired
    private RedissonClient redissonClient;

    private static String PRE_KEY;


    /**
     * lock
     *
     * @param lockKey
     * @return
     */
    public RLock lock(String lockKey) {
        String standardKey = PRE_KEY + ":" + lockKey;
        return redissonClient.getLock(standardKey);
    }

    /**
     * @param key
     */
    public void delete(String key) {
        String standardKey = PRE_KEY + ":" + key;
        redisTemplate.delete(standardKey);
    }


    /**
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        String standardKey = PRE_KEY + ":" + key;
        return redisTemplate.hasKey(standardKey);
    }

    /**
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        String standardKey = PRE_KEY + ":" + key;
        return redisTemplate.expire(standardKey, timeout, unit);
    }


    /**
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        String standardKey = PRE_KEY + ":" + key;
        if (value != null) {
            redisTemplate.opsForValue().set(standardKey, jsonUtil.obj2String(value));
        }
    }

    public void set(String key, Object value, long expire) {
        if (value != null) {
            String standardKey = PRE_KEY + ":" + key;
            redisTemplate.opsForValue().set(standardKey, jsonUtil.obj2String(value), expire, TimeUnit.SECONDS);
        }
    }


    public void setExSeconds(String key, Object value, long expire) {
        if (value != null) {
            String standardKey = PRE_KEY + ":" + key;
            redisTemplate.opsForValue().set(standardKey, jsonUtil.obj2String(value), expire, TimeUnit.SECONDS);
        }
    }

    public Long increment(String key, Long value, Long seconds) {
        String standardKey = PRE_KEY + ":" + key;
        Long increment = redisTemplate.opsForValue().increment(standardKey, value);
        redisTemplate.expire(standardKey, seconds, TimeUnit.SECONDS);
        return increment;
    }


    /**
     * @param key
     * @return
     */
    public String get(String key) {
        String standardKey = PRE_KEY + ":" + key;
        return redisTemplate.opsForValue().get(standardKey);
    }

    public <T> T get(String key, Class<T> clzz) {
        String standardKey = PRE_KEY + ":" + key;
        String val = redisTemplate.opsForValue().get(standardKey);
        return jsonUtil.string2Obj(val, clzz);
    }

    public <T> T get(String key, TypeReference<T> clazz) {
        String standardKey = PRE_KEY + ":" + key;
        String value = redisTemplate.opsForValue().get(standardKey);
        return jsonUtil.string2Obj(value, clazz);
    }


    public void addZSet(String key, Object obj, double co, Long expireSecondsTime) {
        String standardKey = PRE_KEY + ":" + key;
        redisObjTemplate.opsForZSet().add(standardKey, obj, co);
        redisObjTemplate.expire(standardKey, expireSecondsTime, TimeUnit.SECONDS);
    }

    public Set<Object> reverseRangeZSet(String key, Long start, Long end) {
        String standardKey = PRE_KEY + ":" + key;
        return redisObjTemplate.opsForZSet().reverseRange(standardKey, start, end);
    }

    public Set<Object> reverseRangeByScoreZSet(String key, double min, double max) {
        String standardKey = PRE_KEY + ":" + key;
        return redisObjTemplate.opsForZSet().reverseRangeByScore(standardKey, min, max);
    }

    public Long sizeZSet(String key) {
        String standardKey = PRE_KEY + ":" + key;
        return redisObjTemplate.opsForZSet().size(standardKey);
    }


    public void setEx(String key, String value, long timeout, TimeUnit unit) {
        String standardKey = PRE_KEY + ":" + key;
        redisTemplate.opsForValue().set(standardKey, value, timeout, unit);
    }


    /**
     * @param key
     * @param value
     * @return
     */
    public boolean setIfAbsent(String key, String value, long expire, TimeUnit unit) {
        String standardKey = PRE_KEY + ":" + key;
        return redisTemplate.opsForValue().setIfAbsent(standardKey, value, expire, unit);
    }


    public String leftPopRightPushWithLuaList(String key) {
        String standardKey = PRE_KEY + ":" + key;
        // Lua script
        StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("local value = redis.call('LPOP', KEYS[1]);")
                .append(" if value then ")
                .append("redis.call('RPUSH', KEYS[1], value);")
                .append(" end ")
                .append("return value;");
        String script = scriptBuilder.toString();

        return redisTemplate.execute(RedisScript.of(script, String.class),
                Collections.singletonList(standardKey));
    }


    public Long size4list(String key) {
        String standardKey = PRE_KEY + ":" + key;
        return redisTemplate.opsForList().size(standardKey);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        PRE_KEY = env + ":" + serviceName;
    }
}
