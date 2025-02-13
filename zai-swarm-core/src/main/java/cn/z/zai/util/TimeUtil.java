package cn.z.zai.util;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static LocalDateTime epochMilli2DateTime(Long timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static LocalDateTime epochSeconds2DateTime(Long seconds) {
        if (seconds == null || seconds <= 0) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault());
    }

    public static String epochSeconds2DateTimeStr(Long seconds) {
        LocalDateTime localDateTime;
        if (seconds == null || seconds <= 0) {
            localDateTime = currentLocalDateTime();
        } else {
            localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault());
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return localDateTime.format(formatter);
    }


    /**
     * get current second
     *
     * @return
     */
    public static Long currentEpochSecond() {
        // now time
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();

        return instant.getEpochSecond();

    }

    /**
     * get current second -> Minute by minute
     *
     * @return
     */
    public static Long currentEpochSecond4Minute() {
        // now time
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        long currentEpochSecond = zonedDateTime.toInstant().getEpochSecond();
        try {
            return (currentEpochSecond / 60) * 60;
        } catch (Exception e) {
            return currentEpochSecond;
        }
    }

    /**
     * get current second
     *
     * @return
     */
    public static Long localDateTimeToMills(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    }

    /**
     * get current dateTime
     *
     * @return
     */
    public static LocalDateTime currentLocalDateTime() {
        // now time
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime();

    }







    /**
     * dateTime to current
     *
     * @return
     */
    public static Long timestampSeconds(LocalDateTime time) {
        if (Objects.isNull(time)) {
            time = LocalDateTime.now();
        }
        return time.toEpochSecond(ZoneOffset.UTC);

    }

    /**
     * Format to 40d 19h 50m ago
     */
    public static String formatTimeAgo(Long timestampInSeconds) {

        if (Objects.isNull(timestampInSeconds) || timestampInSeconds == 0) {
            timestampInSeconds = currentEpochSecond();
        }
        timestampInSeconds = currentEpochSecond() - timestampInSeconds;

        long days = TimeUnit.SECONDS.toDays(timestampInSeconds);
        long hours = TimeUnit.SECONDS.toHours(timestampInSeconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(timestampInSeconds) % 60;


        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append("d ");
        }
        if (hours > 0 || days > 0) {
            result.append(hours).append("h ");
        }
        if (minutes > 0 || hours > 0 || days > 0) {
            result.append(minutes).append("m ");
        }
        result.append("ago");

        return result.toString().trim();
    }
}
