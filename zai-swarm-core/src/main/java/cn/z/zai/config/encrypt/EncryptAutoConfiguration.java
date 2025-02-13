package cn.z.zai.config.encrypt;

import cn.z.zai.config.encrypt.web.EncryptWebMvcConfiguration;
import cn.z.zai.config.encrypt.web.ResponseControllerAdvice;
import cn.z.zai.config.encrypt.web.filter.WebFilter;
import cn.z.zai.config.encrypt.web.interceptor.AbstractSignAndEncryptInterceptor;
import cn.z.zai.config.encrypt.web.interceptor.DefaultSignAndEncryptInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;


@Slf4j
@Import(ResponseControllerAdvice.class)
@EnableConfigurationProperties(EncryptProperties.class)
@Configuration
public class EncryptAutoConfiguration {

    @Bean
    public FilterRegistrationBean<WebFilter> webFilterRegistrationBean() {

        FilterRegistrationBean<WebFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebFilter());
        bean.setOrder(-2);

        bean.setUrlPatterns(Collections.singletonList("/*"));

        return bean;
    }

    @Bean
    public EncryptWebMvcConfiguration encryptWebMvcConfiguration(AbstractSignAndEncryptInterceptor interceptor) {
        return new EncryptWebMvcConfiguration(interceptor);
    }

    @Bean
    @ConditionalOnMissingBean(AbstractSignAndEncryptInterceptor.class)
    public DefaultSignAndEncryptInterceptor defaultSignAndEncryptInterceptor(EncryptProperties properties) {
        properties.checkParam();
        DefaultSignAndEncryptInterceptor interceptor = new DefaultSignAndEncryptInterceptor(properties);
        log.info("encrypt-spring-boot-start complete");
        return interceptor;
    }
}
