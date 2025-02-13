package cn.z.zai.config.encrypt.web.interceptor;

import cn.z.zai.config.encrypt.EncryptProperties;
import cn.z.zai.config.encrypt.annotation.UnSignAndEncrypt;
import cn.z.zai.config.encrypt.constant.EncryptConstants;
import cn.z.zai.config.encrypt.constant.EncryptContext;
import cn.z.zai.config.encrypt.dto.EncryptRequestBodyVo;
import cn.z.zai.config.encrypt.exception.EncryptException;
import cn.z.zai.config.encrypt.utils.AESUtil;
import cn.z.zai.config.encrypt.utils.DefaultVersionUtil;
import cn.z.zai.config.encrypt.utils.RSAUtil;
import cn.z.zai.config.encrypt.utils.SignUtil;
import cn.z.zai.config.encrypt.web.filter.RequestWrapper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.z.zai.util.ContextHolder;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public abstract class AbstractSignAndEncryptInterceptor implements HandlerInterceptor {

    protected abstract EncryptProperties getProperties();

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getRequestURI().contains("swagger")) {
            return true;
        }


        UnSignAndEncrypt open = ((HandlerMethod) handler).getMethodAnnotation(UnSignAndEncrypt.class);
        if (getProperties().isEnabled() && open == null
                && checkAppVersion(request.getHeader(EncryptConstants.APP_VERSION))
        ) {
            if (getProperties().isEnabledLog()) {
                log.info("start jm: path={}", request.getRequestURI());
            }
            Map<String, String> map = buildHeaderMapWithCheck(request, getProperties());
            if (getProperties().isEnabledLog()) {
                log.info("start jm: path={},header={}", request.getRequestURI(), map);
            }

            checkUniqueness(map.get(EncryptConstants.NONCE), map.get(EncryptConstants.TIMESTAMP));

            boolean verifySign = SignUtil.verifySign(map, getProperties().getSignPublicKey());
            if (!verifySign) {
                throw new EncryptException("sign is invalid");
            }
            String contentType = request.getContentType();
            if (StrUtil.isEmpty(contentType) || !contentType.contains(ContentType.MULTIPART.getValue())) {

                String aesKey =
                        RSAUtil.decrypt(request.getHeader(EncryptConstants.AES_KEY), getProperties().getRsaPrivateKey());
                EncryptContext.setAesKey(aesKey);
                RequestWrapper requestWrapper = (RequestWrapper) request;
                String bodyEncrypt = requestWrapper.getBody();
                if (StrUtil.isNotEmpty(bodyEncrypt)) {
                    Gson gson = new Gson();
                    EncryptRequestBodyVo encryptRequestBodyVo = gson.fromJson(bodyEncrypt, EncryptRequestBodyVo.class);
                    String body = AESUtil.decrypt(encryptRequestBodyVo.getEncryptBody(), aesKey, aesKey);
                    requestWrapper.setBody(body);
                    if (getProperties().isEnabledLog()) {
                        log.info("decrypt-bod: path={},body={}", request.getRequestURI(), body);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        EncryptContext.clean();
    }

    protected abstract void checkUniqueness(String nonce, String timestamp);


    protected boolean checkAppVersion(String appVersion) {
        if (StrUtil.isEmpty(getProperties().getAppVersion())) {
            return true;
        }

        if (StrUtil.isEmpty(appVersion)) {
            log.warn("appVersion is empty, userId is {}", ContextHolder.getUserId());
            return Boolean.TRUE;
        }

        if (StringUtils.equalsAnyIgnoreCase(env, "prd")) {
            return Boolean.TRUE;
        }

        return DefaultVersionUtil.compareVersion(getProperties().getAppVersion(), appVersion) <= 0;
    }

    public static Map<String, String> buildHeaderMapWithCheck(HttpServletRequest request, EncryptProperties properties) {

        String nonce = request.getHeader(EncryptConstants.NONCE);
        String timestamp = request.getHeader(EncryptConstants.TIMESTAMP);
        String aesKey = request.getHeader(EncryptConstants.AES_KEY);
        String sign = request.getHeader(EncryptConstants.SIGN);
        String appVersion = request.getHeader(EncryptConstants.APP_VERSION);

        if (StringUtils.isEmpty(nonce)) {
            if (properties.isEnabledLog()) {
                log.info("nonce can not be null: path={}", request.getRequestURI());
            }
            throw new IllegalArgumentException("nonce can not be null ");
        }
        if (StringUtils.isEmpty(timestamp)) {
            if (properties.isEnabledLog()) {
                log.info("timestamp can not be null: path={}", request.getRequestURI());
            }
            throw new IllegalArgumentException("timestamp can not be null");
        }
        if (StringUtils.isEmpty(aesKey)) {
            if (properties.isEnabledLog()) {
                log.info("aesKey can not be null: path={}", request.getRequestURI());
            }
            throw new IllegalArgumentException("aesKey can not be null");
        }
        if (StringUtils.isEmpty(sign)) {
            if (properties.isEnabledLog()) {
                log.info("sign can not be null: path={}", request.getRequestURI());
            }
            throw new IllegalArgumentException("sign can not be null");
        }

        if (StringUtils.isEmpty(appVersion)) {
            if (properties.isEnabledLog()) {
                log.info("appVersion can not be null: path={}", request.getRequestURI());
            }
            throw new IllegalArgumentException("appVersion can not be null");
        }

        Map<String, String> map = new HashMap<>();
        map.put(EncryptConstants.NONCE, nonce);
        map.put(EncryptConstants.TIMESTAMP, timestamp);
        map.put(EncryptConstants.AES_KEY, aesKey);
        map.put(EncryptConstants.SIGN, sign);
        map.put(EncryptConstants.APP_VERSION, appVersion);
        return map;
    }
}
