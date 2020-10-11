package cn.lijun816.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * JsonUtil
 *
 * @author 87922 2020/10/11
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper mapper;

    private static ObjectMapper getMapper() {
        if (mapper != null) {
            return mapper;
        }
        synchronized (JsonUtil.class) {
            if (mapper != null) {
                return mapper;
            }
            mapper = SpringBeanUtil.getBean(ObjectMapper.class);
        }
        return mapper;
    }

    public static String toJson(Object obj) {
        try {
            return getMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("toJson::{},{}", e.getMessage(), obj, e);
            throw new RuntimeException("toJson错误");
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (IOException e) {
            log.error("toObject::{},{}", e.getMessage(), json, e);
            throw new RuntimeException("toObject错误");
        }
    }

    public static <T> List<T> toListObject(String json, Class<T> clazz) {
        JavaType javaType = getMapper().getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("toListObject::{},{}", e.getMessage(), json, e);
            throw new RuntimeException("toObject错误");
        }
    }
}