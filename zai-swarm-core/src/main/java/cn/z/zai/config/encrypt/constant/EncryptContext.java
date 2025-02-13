package cn.z.zai.config.encrypt.constant;


public class EncryptContext {
    private static final ThreadLocal<String> AES_KEY = new ThreadLocal<>();

    private EncryptContext() {}

    public static void setAesKey(String aesKey) {
        AES_KEY.set(aesKey);
    }

    public static String getAesKey() {
        return AES_KEY.get();
    }

    public static void clean() {
        AES_KEY.remove();
    }
}
