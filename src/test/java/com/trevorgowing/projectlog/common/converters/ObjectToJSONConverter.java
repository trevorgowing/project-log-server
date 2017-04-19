package com.trevorgowing.projectlog.common.converters;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class ObjectToJSONConverter {

    public static String convertToJSON(Object object) throws Exception {
        ObjectWriter objectWriter = Jackson2ObjectMapperBuilder.json().build().writer();
        return objectWriter.writeValueAsString(object);
    }
}
