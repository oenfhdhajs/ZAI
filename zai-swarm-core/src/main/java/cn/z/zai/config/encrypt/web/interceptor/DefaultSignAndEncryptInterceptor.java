package cn.z.zai.config.encrypt.web.interceptor;

import cn.z.zai.config.encrypt.EncryptProperties;
import cn.z.zai.config.encrypt.exception.EncryptException;
import cn.z.zai.util.TimeUtil;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DefaultSignAndEncryptInterceptor extends AbstractSignAndEncryptInterceptor {

    protected EncryptProperties properties;

    public void setProperties(EncryptProperties properties) {
        this.properties = properties;
    }

    @Override
    protected EncryptProperties getProperties() {
        return properties;
    }

    @Override
    protected void checkUniqueness(String nonce, String timestamp) {
        Long aLong = TimeUtil.currentEpochSecond();
        if (Long.valueOf(timestamp).compareTo(aLong - 600) < 0) {
            throw new EncryptException("timestamp expire");
        }

    }
}
