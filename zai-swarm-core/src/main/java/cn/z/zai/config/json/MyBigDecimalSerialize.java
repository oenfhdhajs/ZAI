package cn.z.zai.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;


public class MyBigDecimalSerialize extends JsonSerializer<BigDecimal> {
      @Override
      public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (Objects.nonNull(value)) {
                gen.writeString(value.stripTrailingZeros().toPlainString());
            } else {

                gen.writeNull();
            }
      }
}