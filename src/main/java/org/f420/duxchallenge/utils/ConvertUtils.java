package org.f420.duxchallenge.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.exceptions.ApiException;

import java.io.IOException;

import static org.f420.duxchallenge.enums.ErrorMessage.JSON_PROCESSING_ERROR;

@Getter
@Slf4j
public class ConvertUtils {

    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    public static <T> T convertValue(Object o, Class<T> clazz) {
        if (o == null) {
            return null;
        }
        try {
            return objectMapper.readValue(toObjectString(o), clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(JSON_PROCESSING_ERROR);
        }
    }

    public static String toObjectString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(JSON_PROCESSING_ERROR);
        }
    }

    public static Object convertToFieldType(String value, Class<?> fieldType) {
        if (fieldType.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (fieldType.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (fieldType.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (fieldType.equals(Double.class)) {
            return Double.parseDouble(value);
        } else {
            return value;
        }
    }
}
