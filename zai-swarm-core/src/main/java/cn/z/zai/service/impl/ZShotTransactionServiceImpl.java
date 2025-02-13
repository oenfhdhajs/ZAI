package cn.z.zai.service.impl;

import cn.z.zai.dto.response.ZShotTransactionResponse;
import cn.z.zai.dto.response.ZShotTransactionSignatureResponse;
import cn.z.zai.dto.vo.ZShotTransactionSwapVo;
import cn.z.zai.dto.vo.ZShotTransactionTransferVo;
import cn.z.zai.service.ZShotTransactionService;
import cn.z.zai.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class ZShotTransactionServiceImpl implements ZShotTransactionService {


    @Value(value = "${inner.zShotTransactionUrl}")
    private String zShotTransactionUrl;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ZShotTransactionResponse<ZShotTransactionSignatureResponse> transfer(ZShotTransactionTransferVo vo) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);


            Map<String, Object> requestBody = new ObjectMapper().convertValue(vo, new TypeReference<Map<String, Object>>() {
            });
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


            ResponseEntity<String> response = restTemplate.exchange(
                    zShotTransactionUrl + "/outer/wallet/transfer",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            ZShotTransactionResponse<ZShotTransactionSignatureResponse> result = jsonUtil.string2Obj(response.getBody(), new TypeReference<ZShotTransactionResponse<ZShotTransactionSignatureResponse>>() {
            });
            if (result == null) {
                log.error("zShotTransaction transfer result null, param is {}", vo);
                return null;
            }
            log.info("zShotTransaction transfer success : {}", vo);
            return result;
        } catch (Exception error) {
            log.error("zShotTransaction transfer catch error :", error);
            return null;
        }
    }

    @Override
    public ZShotTransactionResponse<ZShotTransactionSignatureResponse> swap(ZShotTransactionSwapVo vo) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);


            Map<String, Object> requestBody = new ObjectMapper().convertValue(vo, new TypeReference<Map<String, Object>>() {
            });
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


            ResponseEntity<String> response = restTemplate.exchange(
                    zShotTransactionUrl + "/outer/wallet/swap",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            ZShotTransactionResponse<ZShotTransactionSignatureResponse> result = jsonUtil.string2Obj(response.getBody(), new TypeReference<ZShotTransactionResponse<ZShotTransactionSignatureResponse>>() {
            });
            if (result == null) {
                log.error("zShotTransaction swap result null, param is {}", vo);
                return null;
            }
            log.info("zShotTransaction swap success : {}", vo);
            return result;
        } catch (Exception error) {
            log.error("zShotTransaction swap catch error :", error);
            return null;
        }
    }


}
