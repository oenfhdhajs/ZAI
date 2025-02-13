package cn.z.zai.util;


import java.math.BigInteger;

public class ContextHolder {
    private static final ThreadLocal<String> REQUEST_ID_THREAD_LOCAL = new ThreadLocal();
    private static final ThreadLocal<BigInteger> TG_USER_ID_THREAD_LOCAL = new ThreadLocal();

    private static final ThreadLocal<String> IP_THREAD_LOCAL = new ThreadLocal();

    private static final ThreadLocal<String> APP_VERSION = new ThreadLocal();

    private ContextHolder() {
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID_THREAD_LOCAL.set(requestId);
    }

    public static String getRequestId() {
        return REQUEST_ID_THREAD_LOCAL.get();
    }

    public static void setUserId(BigInteger userId) {
        TG_USER_ID_THREAD_LOCAL.set(userId);
    }

    public static BigInteger getUserId() {
        return TG_USER_ID_THREAD_LOCAL.get();
    }

    public static void setIp(String ip) {
        IP_THREAD_LOCAL.set(ip);
    }

    public static String getIp() {
        return IP_THREAD_LOCAL.get();
    }

    public static void setAppVersion(String appVersion) {
        APP_VERSION.set(appVersion);
    }

    public static String getAppVersion() {
        return APP_VERSION.get();
    }

    public static void clear() {
        REQUEST_ID_THREAD_LOCAL.remove();
        TG_USER_ID_THREAD_LOCAL.remove();
        IP_THREAD_LOCAL.remove();
        APP_VERSION.remove();
    }
}
