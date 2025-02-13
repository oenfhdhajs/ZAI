package cn.z.zai.config.encrypt.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class RSAUtil {

    /**
     * RSA
     **/
    private static final String ALGORITHM_RSA = "RSA";

    private static final String RSA_SIGNATURE = "SHA1WithRSA";

    /**
     *
     *
     * @param data
     * @param privateKeyStr
     */
    public static String sign(String data, String privateKeyStr) {
        try {

            PrivateKey key = KeyFactory.getInstance(ALGORITHM_RSA)
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr)));

            Signature signature = Signature.getInstance(RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(DigestUtils.md5DigestAsHex(data.getBytes()).getBytes());
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            log.error("sign false: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     *
     *
     * @param data
     * @param publicKeyStr
     * @param sign
     * @return
     */
    public static boolean verify(String data, String publicKeyStr, String sign) {
        try {

            PublicKey key = KeyFactory.getInstance(ALGORITHM_RSA)
                .generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr)));

            Signature signature = Signature.getInstance(RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(DigestUtils.md5DigestAsHex(data.getBytes()).getBytes());
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            log.error("verify sign false: ", e);
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static Map<String, String> generateKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            Map<String, String> map = new HashMap();
            map.put("publicKey", Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
            map.put("privateKey", Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
            return map;
        } catch (Exception e) {
            log.error("creat generateKey error: ", e);
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String data, String publicKeyStr) {
        try {
            PublicKey publicKey = KeyFactory.getInstance(ALGORITHM_RSA)
                .generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr)));
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            log.error("sign exception: ", e);
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptedData, String privateKeyStr) throws Exception {
        PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM_RSA)
            .generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr)));
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedData));
        return new String(decryptedBytes);
    }

    public static byte[] encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes);
    }






}
