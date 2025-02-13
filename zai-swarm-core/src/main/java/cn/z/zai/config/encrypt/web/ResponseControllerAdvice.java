package cn.z.zai.config.encrypt.web;

import cn.z.zai.config.encrypt.annotation.UnSignAndEncrypt;
import cn.z.zai.config.encrypt.constant.EncryptContext;
import cn.z.zai.config.encrypt.utils.AESUtil;
import cn.z.zai.config.encrypt.EncryptProperties;
import cn.z.zai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private EncryptProperties properties;

    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        UnSignAndEncrypt methodAnnotation = methodParameter.getMethodAnnotation(UnSignAndEncrypt.class);
        return properties.isEnabled() && methodAnnotation == null && StringUtils.isNotBlank(EncryptContext.getAesKey());
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter, MediaType mediaType,
        Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
        ServerHttpResponse serverHttpResponse) {
        if (properties.isEnabledLog()) {
            log.info("jm ok :path={},result={}", serverHttpRequest.getURI().getPath(), jsonUtil.obj2String(data));
        }
        if (data == null) {
            return data;
        }

        Map<String, Object> map = jsonUtil.string2Obj(jsonUtil.obj2String(data), HashMap.class);

        Object businessData = map.get(properties.getBusinessDataName());
        if (businessData != null) {
            map.put(properties.getBusinessDataName(), AESUtil.encrypt(jsonUtil.obj2String(businessData),
                EncryptContext.getAesKey(), EncryptContext.getAesKey()));
        }
        return map;
    }
}
