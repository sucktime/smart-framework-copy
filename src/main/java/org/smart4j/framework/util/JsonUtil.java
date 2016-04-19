package org.smart4j.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Bomb on 16/4/13.
 *
 * @author mrJ
 * @since 1.0.0
 * <p>
 * POJO 与 JSON 互转
 */

public final class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将 POJO 转为 JSON
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJson(T obj) {
        String jsonString;
        try {
            jsonString = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("convert POJO to JOSN failure.", e);
            throw new RuntimeException(e);
        }
        return jsonString;
    }

    public static <T> T fromJson(String jsonString, Class<T> type) {
        T pojo;
        try {
            pojo = OBJECT_MAPPER.readValue(jsonString, type);
        } catch (IOException e) {
            LOGGER.error("convert JSON to POJO failure.", e);
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
