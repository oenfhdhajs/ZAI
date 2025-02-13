package cn.z.zai.config.asyn;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Description:**
 */
public class IdTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> map = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (!CollectionUtils.isEmpty(map)) {
                    MDC.setContextMap(map);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };

    }
}
