package cn.z.zai.util;

import org.apache.commons.lang3.StringUtils;


public class RegularUtils {

    public static boolean includingChinese(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        String regex = "[\u4e00-\u9fa5]";
        if (str.matches(".*" + regex + ".*")) {
            return true;
        } else {
            return false;
        }
    }

}
