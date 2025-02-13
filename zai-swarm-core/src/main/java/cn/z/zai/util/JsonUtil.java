package cn.z.zai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Objects;

@Component
public class JsonUtil {

    @Autowired
    private ObjectMapper objectMapperAuto;
    private static  ObjectMapper objectMapper;
    public <T> String obj2String(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }



    @SuppressWarnings("unchecked")
    public <T> T string2Obj(String str,Class<T> clazz){
        if(isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T string2Obj(String str, Type type){
        if(isEmpty(str) || type == null){
            return null;
        }
        try {
            return type.equals(String.class)? (T)str : objectMapper.readValue(str, objectMapper.getTypeFactory().constructType(type));
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)? str : objectMapper.readValue(str,typeReference));
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T string2Obj(InputStream reader, TypeReference<T> typeReference){
        if(reader == null || typeReference == null){
            return null;
        }
        try {
            return (T)objectMapper.readValue(reader,typeReference);
        } catch (Exception e) {
            return null;
        }
    }


    public <T> T string2Obj(String str,Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            return null;
        }
    }


    private static boolean isEmpty(String str) {
        return str == null || (str.isEmpty());
    }


    @PostConstruct
    public void initProperties() {
        if (Objects.isNull(objectMapper)) {
            objectMapper = objectMapperAuto;
        }

    }
}
