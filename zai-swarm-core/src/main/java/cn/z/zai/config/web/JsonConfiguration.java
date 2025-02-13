package cn.z.zai.config.web;

import cn.z.zai.config.NullValueSerializer;
import cn.z.zai.config.json.LocalDateFromEpochDeserializer;
import cn.z.zai.config.json.LocalDateTimeFromEpochDeserializer;
import cn.z.zai.config.json.LocalDateTimeToEpochSerializer;
import cn.z.zai.config.json.LocalDateToEpochSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.*;
import java.util.Date;

@Configuration
public class JsonConfiguration {
    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeToEpochSerializer())
                .deserializerByType(LocalDateTime.class, new LocalDateTimeFromEpochDeserializer())
                .serializerByType(LocalDate.class,new LocalDateToEpochSerializer())
                .deserializerByType(LocalDate.class,new LocalDateFromEpochDeserializer());
    }


    /**
     * Json serialization and deserialization converter, used to convert JSON in the body of Post requests and serialize our objects into JSON that returns a response
     */
    //@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializerProvider().setNullValueSerializer(NullValueSerializer.instance);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // Cancel default conversion of timestamps format
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // Ignoring the error of converting empty beans to JSON
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // LocalDateTime series serialization and deserialization modules, inherited from jsr310, where we have modified the date format
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeToEpochSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeFromEpochDeserializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateToEpochSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateFromEpochDeserializer());

        // Date serialization and deserialization
        javaTimeModule.addSerializer(Date.class, new JsonSerializer<Date>() {

            @Override
            public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeNumber(date.getTime());
            }
        });
        javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String dateStr = jsonParser.getText();
                return new Date(Long.parseLong(dateStr));
            }
        });


        SimpleModule simpleModule = new SimpleModule();
       // simpleModule.addSerializer(Number.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }

}
