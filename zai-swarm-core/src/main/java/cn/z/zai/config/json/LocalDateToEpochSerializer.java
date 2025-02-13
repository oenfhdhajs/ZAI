package cn.z.zai.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class LocalDateToEpochSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            LocalDateTime localDateTimeValue = LocalDateTime.of(value, LocalTime.of(0, 0, 0));
            long timestamp = localDateTimeValue.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
            gen.writeNumber(timestamp);
        }
    }
}
