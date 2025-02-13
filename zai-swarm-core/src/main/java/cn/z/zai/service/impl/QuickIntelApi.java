package cn.z.zai.service.impl;

import cn.z.zai.common.constant.QuickIntelApiConstant;
import cn.z.zai.dto.response.QuickIntelResponseQuickiauditfull;
import cn.z.zai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class QuickIntelApi implements QuickIntelApiConstant {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JsonUtil jsonUtil;

    /**
     * https://developer.quickintel.io/api-details#api=quick-intel-api-subscription&operation=Get-Full-Audit
     */
    public QuickIntelResponseQuickiauditfull getTokenSecretInfo(String tokenAddress) {
        try {
            HashMap<String, Object> body = new HashMap<>();
            body.put("chain", "solana");
            body.put("tokenAddress", tokenAddress);
            HttpEntity<Object> httpEntity = packageHeaders(jsonUtil.string2Obj(jsonUtil.obj2String(body), HashMap.class));
            ResponseEntity<String> exchange = restTemplate.exchange(
                    URL_POST_AUDIT_FULL,
                    HttpMethod.POST,
                    httpEntity,
                    String.class);
            if (exchange.getStatusCode() != HttpStatus.OK) {
                log.error("QuickIntel API ERROR, body:[{}], : {}", body, exchange.getBody());
            }
            String responseBody = exchange.getBody();
            QuickIntelResponseQuickiauditfull quickiauditfull = jsonUtil.string2Obj(responseBody, QuickIntelResponseQuickiauditfull.class);
            return quickiauditfull;
        } catch (Exception error) {
            log.error("getTokenSecretInfo exception {} {}", error, tokenAddress);
        }
        return null;
    }

    private HttpEntity<Object> packageHeaders(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HEADER_X_QKNTL_KEY, HEADER_X_QKNTL_VALUE);
        return new HttpEntity<>(body, headers);
    }
}
