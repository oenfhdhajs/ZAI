package cn.z.zai.config.encrypt.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


@Slf4j
public class AESUtil {

    private static final String ALGORITHM_AES = "AES";

    /**
     * mode
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * mode
     */
    private static final String CBC_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * encrypt
     *
     * @param content content
     * @param key     key
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key) {
        try {
            byte[] raw = key.getBytes();
            SecretKeySpec skSpec = new SecretKeySpec(raw, ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skSpec);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            return new BASE64Encoder().encode(encrypted).replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\r", "");
        } catch (Exception e) {
            log.error("AES encrypt error: content={},key={}", content, key, e);
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String content, String key) {
        try {
            content = content.replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\r", "");
            byte[] raw = key.getBytes();
            SecretKeySpec skSpec = new SecretKeySpec(raw, ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skSpec);
            byte[] encrypted = new BASE64Decoder().decodeBuffer(content);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original);
        } catch (Exception e) {
            log.error("AES decrypt error: content={},key={}", content, key, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * encrypt
     *
     * @param content
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key, String iv) {
        try {
            byte[] raw = key.getBytes();
            SecretKeySpec skSpec = new SecretKeySpec(raw, ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(CBC_ALGORITHM);

            IvParameterSpec ivParam = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skSpec, ivParam);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            return new BASE64Encoder().encode(encrypted).replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\r", "");
        } catch (Exception e) {
            log.error("AES move encrypt error: content={},key={}", content, key, e);
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String content, String key, String iv) throws Exception {
        content = content.replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\r", "");
        byte[] raw = key.getBytes();
        SecretKeySpec skSpec = new SecretKeySpec(raw, ALGORITHM_AES);
        Cipher cipher = Cipher.getInstance(CBC_ALGORITHM);

        IvParameterSpec ivParam = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skSpec, ivParam);

        byte[] encrypted = new BASE64Decoder().decodeBuffer(content);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }

}
