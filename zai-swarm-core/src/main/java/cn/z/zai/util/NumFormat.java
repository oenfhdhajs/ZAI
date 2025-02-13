package cn.z.zai.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;


public class NumFormat {

    public static String formatBigInteger(BigInteger mktCap) {
        if (Objects.isNull(mktCap)) {
            return "0";
        }
        if (mktCap.compareTo(new BigInteger("1000000000000000000")) >= 0) {
            return String.format("%.1fE", mktCap.divide(new BigInteger("1000000000000000000")).doubleValue());
        } else if (mktCap.compareTo(new BigInteger("1000000000000000")) >= 0) {
            return String.format("%.1fP", mktCap.divide(new BigInteger("1000000000000000")).doubleValue());
        } else if (mktCap.compareTo(new BigInteger("1000000000000")) >= 0) {
            return String.format("%.1fT", mktCap.divide(new BigInteger("1000000000000")).doubleValue());
        } else if (mktCap.compareTo(new BigInteger("1000000000")) >= 0) {
            return String.format("%.1fB", mktCap.divide(new BigInteger("1000000000")).doubleValue());
        } else if (mktCap.compareTo(new BigInteger("1000000")) >= 0) {
            return String.format("%.1fM", mktCap.divide(new BigInteger("1000000")).doubleValue());
        } else if (mktCap.compareTo(new BigInteger("1000")) >= 0) {
            return String.format("%.1fK", mktCap.divide(new BigInteger("1000")).doubleValue());
            // K
        } else {
            return mktCap.toString();
        }
    }

    public static String formatBigDecimal(BigDecimal mktCap) {
        if (Objects.isNull(mktCap)) {
            return "0";
        }
        if (mktCap.compareTo(new BigDecimal("1000000")) >= 0) {
            return String.format("%.2fM",
                    mktCap.divide(new BigDecimal("1000000"), 2, RoundingMode.HALF_UP).doubleValue());
        } else if (mktCap.compareTo(new BigDecimal("1000")) >= 0) {
            return String.format("%.2fK", mktCap.divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP).doubleValue());
            // K
        } else if (mktCap.compareTo(new BigDecimal("0.01")) >= 0) {
            return String.format("%.2f", mktCap);

        } else if (mktCap.compareTo(new BigDecimal("0.0001")) >= 0) {
            return String.format("%.4f", mktCap);

        } else if (mktCap.compareTo(new BigDecimal("0.000001")) >= 0) {
            return String.format("%.6f", mktCap);

        } else if (mktCap.compareTo(new BigDecimal("0.00000001")) >= 0) {
            return String.format("%.8f", mktCap);

        } else if (mktCap.compareTo(new BigDecimal("0.0000000001")) >= 0) {
            return String.format("%.10f", mktCap);

        } else {
            return "0";
        }
    }
}
