package com.etnetera.hr.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}