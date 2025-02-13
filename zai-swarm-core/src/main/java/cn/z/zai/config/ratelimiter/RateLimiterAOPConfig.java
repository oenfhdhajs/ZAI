package cn.z.zai.config.ratelimiter;

import org.springframework.context.annotation.Configuration;



@Configuration
public class RateLimiterAOPConfig {

    //@Bean
    public RateLimiterAOP rateLimiter(){
        return new RateLimiterAOP();
    }
}
