package cn.z.zai.config.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 *  @Description:
 */
public class DefaultLocaleResolver implements LocaleResolver {

    private final static String LANGUAGE_HEADER_KEY = "lang";
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String language = httpServletRequest.getHeader(LANGUAGE_HEADER_KEY);
        Locale locale = Locale.getDefault();
        if (StringUtils.isNotEmpty(language)) {
            String[] split = language.toLowerCase().split("_");
            if (split.length > 1) {
                locale = new Locale(split[0],split[1]);
            }else {
                locale = new Locale(split[0]);
            }
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        //nothing to do
    }
}
