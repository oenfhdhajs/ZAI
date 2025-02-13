package cn.z.zai.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.ContentType;
import cn.z.zai.dto.request.chat.DefaultSseStreamListener;
import cn.z.zai.dto.request.chat.OpenHttpRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class OpenHttpUtil {

    @Autowired
    private JsonUtil jsonUtil;

    private static OkHttpClient okHttpClient;

    static {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(600, TimeUnit.SECONDS);
        client.readTimeout(600, TimeUnit.SECONDS);
        okHttpClient = client.build();
    }

    public void streamPost(OpenHttpRequest openHttpRequest, EventSourceListener eventSourceListener) {
        auth(openHttpRequest);
        asyncRequest(openHttpRequest, eventSourceListener);
    }

    public void asyncRequest(OpenHttpRequest baseHttpRequest, EventSourceListener eventSourceListener) {
        try {
            EventSource.Factory factory = EventSources.createFactory(okHttpClient);
            Request.Builder requestBuild = new Request.Builder()
                    .url(baseHttpRequest.getUrl())
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.toString()), jsonUtil.obj2String(baseHttpRequest.getBodyParams())));
            if (CollUtil.isNotEmpty(baseHttpRequest.getHeaders())) {
                Headers.Builder builder = new Headers.Builder();
                for (String key : baseHttpRequest.getHeaders().keySet()) {
                    builder.add(key, baseHttpRequest.getHeaders().get(key));
                }
                requestBuild.headers(builder.build());
            }
            factory.newEventSource(requestBuild.build(), eventSourceListener);
        } catch (Exception e) {
            log.error("asyncRequest ERROR:{}", jsonUtil.obj2String(baseHttpRequest), e);
            DefaultSseStreamListener listener = (DefaultSseStreamListener) eventSourceListener;
            BaseSseEmitterServerUtil.close(listener.getConnId());
        }
    }

    private static void auth(OpenHttpRequest openHttpRequest) {
        Map<String, String> headers = openHttpRequest.getHeaders();
        if (headers == null) {
            headers = new HashMap<>();
            openHttpRequest.setHeaders(headers);
        }
        headers.put("Authorization", "Bearer " + openHttpRequest.getAuthorization());
    }
}
