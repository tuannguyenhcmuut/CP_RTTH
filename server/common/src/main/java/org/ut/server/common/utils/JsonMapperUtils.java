package org.ut.server.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ut.server.common.exception.ErrorResponse;

public class JsonMapperUtils {
    public static ErrorResponse toErrorResponseJson(String responseStr) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse jsonMap = mapper.readValue(responseStr, ErrorResponse.class);
        return jsonMap;
    }
}
