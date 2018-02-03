package org.huiche.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.huiche.core.exception.SystemError;
import org.huiche.core.exception.BaseException;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author Maning
 */
public class JsonUtil extends ObjectMapper {
    public JsonUtil() {
        super();
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, false);
    }


    public String toJson(Object value) {
        if (null == value) {
            return "";
        }
        try {
            return this.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new BaseException(SystemError.JSON_ERROR);
        }
    }

    public String toJsonPretty(Object value) {
        if (null == value) {
            return "";
        }
        try {
            if (value instanceof String) {
                return this.writerWithDefaultPrettyPrinter().writeValueAsString(fromJson((String) value, Object.class));
            } else {
                return this.writerWithDefaultPrettyPrinter().writeValueAsString(value);
            }
        } catch (IOException e) {
            throw new BaseException(SystemError.JSON_ERROR);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        if (null == json) {
            return null;
        }
        try {
            return this.readValue(json, clazz);
        } catch (IOException e) {
            throw new BaseException(SystemError.JSON_ERROR);
        }
    }

    public <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (null == json) {
            return null;
        }
        try {
            return this.readValue(json, typeReference);
        } catch (IOException e) {
            throw new BaseException(SystemError.JSON_ERROR);
        }
    }

    public <T> T fromJson(String json, JavaType type) {
        if (null == json) {
            return null;
        }
        try {
            return this.readValue(json, type);
        } catch (IOException e) {
            throw new BaseException(SystemError.JSON_ERROR);
        }
    }
}
