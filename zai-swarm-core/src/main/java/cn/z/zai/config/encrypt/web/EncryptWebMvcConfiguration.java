package cn.z.zai.config.encrypt.web;

import cn.z.zai.config.encrypt.web.interceptor.AbstractSignAndEncryptInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@AllArgsConstructor
public class EncryptWebMvcConfiguration implements WebMvcConfigurer {

    private AbstractSignAndEncryptInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }

}
