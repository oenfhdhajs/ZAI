package cn.z.zai.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateFromEpochDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        NumberDeserializers.LongDeserializer longDeserializer = new NumberDeserializers.LongDeserializer(Long.TYPE, 0L);
        Long epoch = longDeserializer.deserialize(p, ctxt);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
        return dateTime.toLocalDate();
    }
}
