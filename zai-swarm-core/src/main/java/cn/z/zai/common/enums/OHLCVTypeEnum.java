package cn.z.zai.common.enums;

import lombok.Getter;

@Getter
public enum OHLCVTypeEnum {

    A_MINUTE("1m", "A minute"),
    // THREE_MINUTES("3m", "Three minutes"),
    FIVE_MINUTES("5m", "Five Minutes"),
    // FIFTEEN_MINUTES("15m", "Fifteen minutes"),

    // THIRTY_MINUTES("30m", "Thirty minutes"),
    AN_HOUR("1H", "An hour"),

    // TWO_HOURS("2H","Two hours"),
    FOUR_HOURS("4H", "four hours"),
    // SIX_HOURS("6H","Six Hours"),
    // EIGHT_HOURS("8H","Eight hours"),
    // TWELVE_HOURS("12H","Twelve hours"),
    A_DAYS("1D", "A days"),
    // THREE_DAYS("3D","Three days"),
    // A_WEEK("1W","A week"),
    // ONE_MONTH("1M","one month"),
    THREE_DAYS("3D", "Three days"),
    ;

    private final String type;
    private final String message;

    OHLCVTypeEnum(String type, String message) {
        this.type = type;
        this.message = message;
    }
}
