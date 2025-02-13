package cn.z.zai.config.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 *  @Description:
 */
//@Configuration
public class I18nConfiguration {


    @Bean
    public LocaleResolver localeResolver() {
        return new DefaultLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // param name
        lci.setParamName("lang");
        return lci;
    }

}
