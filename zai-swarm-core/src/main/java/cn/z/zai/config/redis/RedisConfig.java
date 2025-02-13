package cn.z.zai.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @Description
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        // Factory
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //Set Serialize
        setSerializeConfig(redisTemplate);
        return redisTemplate;
    }
    private void setSerializeConfig(RedisTemplate<String, Object> redisTemplate) {
        // The normal serialization method for strings is suitable for keys because we usually use simple strings as keys
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // Ordinary string type keys are serialized using regular serialization methods
        redisTemplate.setKeySerializer(stringRedisSerializer);

        // Regular hash type keys also use regular serialization methods
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // To solve the problem of query cache conversion exceptions, if you can't understand it, just use it directly. This is the Jackson serialization class that comes with Spring Boot, but there may be some issues
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // Ordinary values are automatically serialized using the Jackson method
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // Hash type values are also serialized using the Jackson method
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        // After the property setting is completed, the afterPropertiesSet will be called, and some default handling can be done for unsuccessful settings
        redisTemplate.afterPropertiesSet();
    }

}


