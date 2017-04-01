package com.trevorgowing.projectlog.common.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ObjectToJSON {

    public static String convertToJSON(Object object) throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer();
        return objectWriter.writeValueAsString(object);
    }
}
