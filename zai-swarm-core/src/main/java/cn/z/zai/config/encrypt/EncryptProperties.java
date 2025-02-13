package cn.z.zai.config.encrypt;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "encrypt")
public class EncryptProperties {

    private boolean enabled = true;

    /**
     * enble log
     */
    private boolean enabledLog = false;

    /**
     * name
     */
    private String businessDataName = "data";

    private String rsaPublicKey;

    private String rsaPrivateKey;

    private String signPublicKey;

    private String signPrivateKey;

    private String appVersion;

    public void checkParam() {
        if (enabled == true) {
            if (StringUtils.isEmpty(rsaPrivateKey)) {
                throw new IllegalArgumentException("rsa must be exist in profile");
            }
            if (StringUtils.isEmpty(signPublicKey)) {
                throw new IllegalArgumentException("signRsa must be exist in profile");
            }
        }
    }
}
