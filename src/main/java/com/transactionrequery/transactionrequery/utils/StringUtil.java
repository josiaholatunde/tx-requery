package com.transactionrequery.transactionrequery.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.json.JsonSanitizer;

import java.io.IOException;

public class StringUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T stringToObjectIgnoringUnknownProperties(String data, Class<T> tClass) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(data, tClass);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static String objectToString(Object object) throws Exception {
        try {

            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
            return JsonSanitizer.sanitize(ow.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }
    }
}
