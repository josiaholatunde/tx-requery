package com.transactionrequery.transactionrequery.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Util {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToJson(Object object) throws JsonProcessingException {
      try {
          ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
          String result = objectWriter.writeValueAsString(object);
          return result;
      } catch (Exception ex) {
          return "{}";
      }
    }
}
