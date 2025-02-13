package cn.z.zai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.z.zai.common.constant.CookieFunApiConstant;
import cn.z.zai.dto.response.CookieFunContractAddressReq;
import cn.z.zai.dto.response.CookieFunContractAddressResp;
import cn.z.zai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;


@Slf4j
@Component
public class CookieFunApi implements CookieFunApiConstant {

    String HEADER_API_KEY_NAME = "x-api-key";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JsonUtil jsonUtil;

    public CookieFunContractAddressResp cookieFunContractAddress(String address) {

        CookieFunContractAddressReq cookieFunContractAddressReq = new CookieFunContractAddressReq();

        String url = String.format(CookieFunApiConstant.CONTRACT_ADDRESS, address);
        CookieFunContractAddressResp responses = null;
        try {
            responses = responseByRemote(url, cookieFunContractAddressReq,
                    new ParameterizedTypeReference<CookieFunContractAddressResp>() {
                    });
        } catch (Exception e) {
            log.error("cookieFunContractAddress by address response is error, errMsg is{} address:{}", e.getMessage(),
                    address);

            log.error("cookieFunContractAddress by address response is error, err is{} address:{}", e, address);
        }

        if (responses == null) {
            log.error("cookieFunContractAddress by address response is NULL, address:{}", address);
        }
        return responses;
    }

    private <T> T responseByRemote(String url, Object request, ParameterizedTypeReference<T> responseType) {
        HttpEntity<Object> httpEntity = packageHeaders();
        HashMap hashMap = jsonUtil.string2Obj(jsonUtil.obj2String(request), HashMap.class);
        ResponseEntity<T> exchange =
                restTemplate.exchange(packageUrlParam(url, hashMap), HttpMethod.GET, httpEntity, responseType);
        log.info("coin CookieFun API ResponseStatus :{}", exchange.getStatusCode());
        return exchange.getBody();
    }

    private String packageUrlParam(String baseUrl, Map<String, Object> map) {
        if (CollUtil.isNotEmpty(map)) {
            StringJoiner joiner = new StringJoiner("&");
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value != null && StringUtils.isNotEmpty(value.toString())) {
                    try {
                        joiner.add(key + "=" + URLEncoder.encode(value.toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            String separator = baseUrl.contains("?") ? "&" : "?";
            baseUrl = baseUrl + separator + joiner.toString();
        }
        return baseUrl;
    }

    private HttpEntity<Object> packageHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HEADER_API_KEY_NAME, CookieFunApiConstant.HEADER_API_KEY_VALUE);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        return httpEntity;
    }
}
