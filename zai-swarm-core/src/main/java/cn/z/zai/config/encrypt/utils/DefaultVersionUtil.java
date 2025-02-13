package cn.z.zai.config.encrypt.utils;

import cn.hutool.core.util.StrUtil;

public class DefaultVersionUtil {

    public static boolean validVersion(String version) {
        String reg = "\\d+(.\\d+)*";
        if (StrUtil.isEmpty(version)) {
            return false;
        }
        return version.matches(reg);
    }

    public static int compareVersion(String version1, String version2) {
        if (StrUtil.isBlank(version1) && StrUtil.isNotBlank(version2)) {
            return -1;
        }
        if (StrUtil.isBlank(version2) && StrUtil.isNotBlank(version1)) {
            return 1;
        }
        if (version1.trim().equals(version2.trim())) {
            return 0;
        }
        String[] v1Array = version1.split("\\.");
        String[] v2Array = version2.split("\\.");
        int v1Len = v1Array.length;
        int v2Len = v2Array.length;
        int baseLen = 0;
        if (v1Len > v2Len) {
            baseLen = v2Len;
        } else {
            baseLen = v1Len;
        }

        for (int i = 0; i < baseLen; i++) {
            if (v1Array[i].equals(v2Array[i])) {
                continue;
            } else {
                return Integer.parseInt(v1Array[i]) > Integer.parseInt(v2Array[i]) ? 1 : -1;
            }
        }

        if (v1Len != v2Len) {
            return v1Len > v2Len ? 1 : -1;
        } else {

            return 0;
        }
    }


}
