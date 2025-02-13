package cn.z.zai.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.*;

public class NullValueSerializer extends JsonSerializer<Object> {

    public static final NullValueSerializer instance = new NullValueSerializer();

    @SneakyThrows
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String fieldName = jsonGenerator.getOutputContext().getCurrentName();
        Field field = getField(jsonGenerator.getCurrentValue().getClass(), fieldName);
        if (field != null) {
            Class<?> clazz = field.getType();
            if (CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)) {
                jsonGenerator.writeString("");
            } else if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            } else if (clazz.equals(Boolean.class)) {
                jsonGenerator.writeBoolean(false);
            } else if (Number.class.isAssignableFrom(clazz)) {
                jsonGenerator.writeNumber("0");
            } else if (Map.class.isAssignableFrom(clazz)) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            } else if (Temporal.class.isAssignableFrom(clazz)) {
                jsonGenerator.writeNumber("0");
//                jsonGenerator.writeString("");
            } else {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            }
        }
    }

    static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
}
