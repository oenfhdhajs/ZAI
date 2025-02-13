package cn.z.zai.config.encrypt.utils;

import cn.z.zai.config.encrypt.constant.EncryptConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;


@Slf4j
public class SignUtil {

    public static boolean verifySign(Map<String, String> map, String rsaPublicKey) {

        String sign = map.get(EncryptConstants.SIGN);
        map.remove(EncryptConstants.SIGN);
        String signData = getSignString(map);
        return RSAUtil.verify(signData, rsaPublicKey, sign);
    }

    public static String sign(Map<String, String> map, String rsaPrivateKey) {

        map.remove(EncryptConstants.SIGN);
        String signData = getSignString(map);
        return RSAUtil.sign(signData, rsaPrivateKey);
    }

    /**
     *
     * 
     * @param map
     * @return
     */
    private static String getSignString(Map<String, String> map) {

        Set<String> set = map.keySet();

        Object[] arr = set.toArray();

        Arrays.sort(arr);

        StringBuilder signString = new StringBuilder();
        for (Object key : arr) {
            if (!key.equals(EncryptConstants.SIGN)) {
                signString.append(key);
                Object value = map.get(key.toString());
                signString.append(value.toString());
            }
        }
        return signString.toString();
    }
}
