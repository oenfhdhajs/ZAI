package cn.z.zai.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class CommonUtils {


    public static BigDecimal getTokens(Long amount, Integer decimal) {

        if (Objects.isNull(amount) || Objects.equals(amount, 0L)) {
            return BigDecimal.ZERO;
        }
        if (Objects.isNull(decimal) || Objects.equals(decimal, 0L)) {
            return BigDecimal.ZERO;
        }

        /*int scale = 0;
        if (decimal >0) {
            scale = (int) Math.log10(decimal);
        }*/

        return new BigDecimal(amount).divide(BigDecimal.valueOf(Math.pow(10, decimal)), 6, RoundingMode.HALF_UP);
    }

    public static Long getAmount(BigDecimal tokens, Integer decimal) {

        if (Objects.isNull(tokens)) {
            return 0L;
        }
        if (Objects.isNull(decimal) || Objects.equals(decimal, 0L)) {
            return 0L;
        }
        /*int scale = 0;
        if (decimal >0) {
            scale = (int) Math.log10(decimal);
        }*/
        return tokens.multiply(BigDecimal.valueOf(Math.pow(10, decimal))).longValue();

    }


}
