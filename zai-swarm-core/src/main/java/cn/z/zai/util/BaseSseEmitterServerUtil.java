package cn.z.zai.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
public class BaseSseEmitterServerUtil {

    /**
     * conn count
     */
    private static final AtomicInteger COUNT = new AtomicInteger(0);

    /**
     * SseEmitter
     */
    private static final Map<String, SseEmitter> SSE_EMITTER_MAP = new ConcurrentHashMap<>();

    public static synchronized SseEmitter conn(String connectId ,Long timeOutMillis){
        SseEmitter sseEmitter = SSE_EMITTER_MAP.get(connectId);
        if (sseEmitter == null) {
            sseEmitter = new SseEmitter(timeOutMillis);
            sseEmitter.onCompletion(() -> {
                removeConn(connectId);
            });
            sseEmitter.onTimeout(() -> {
                removeConn(connectId);
            });
            sseEmitter.onError(throwable -> {
                removeConn(connectId);
            });
            SSE_EMITTER_MAP.put(connectId, sseEmitter);

            COUNT.getAndIncrement();
            log.info("create conn success {} ,current conn count {} ", connectId, COUNT.get());
        }
        return sseEmitter;
    }

    public static SseEmitter get(String connectId){
        return SSE_EMITTER_MAP.get(connectId);
    }

    public static void close(String connectId){
        removeConn(connectId);
    }

    public static void removeConn(String connectId) {
        SseEmitter sseEmitter = SSE_EMITTER_MAP.get(connectId);
        if (sseEmitter != null){
            COUNT.getAndDecrement();
            SSE_EMITTER_MAP.remove(connectId);
            sseEmitter.complete();
        }
        log.info("conn close finish {},current conn count {}",connectId,COUNT.get());
    }

    /**
     * @param messageId
     * @param data
     * @return
     */
    public static Boolean sendMessage(String messageId,String data){
        SseEmitter sseEmitter = get(messageId);
        if (sseEmitter == null){
            log.error("ERROR get conn is null {}", messageId);
            return Boolean.FALSE;
        }
        try {
            sseEmitter.send(data);
            return Boolean.TRUE;
        } catch (IOException e) {
            log.error("send message ERROR{}",messageId,e);
        }
        return Boolean.FALSE;
    }

}
