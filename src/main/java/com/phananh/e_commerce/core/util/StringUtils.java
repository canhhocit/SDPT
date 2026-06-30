package com.phananh.e_commerce.core.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StringUtils {

//    public static String normalizeName(String name) throws IOException {
//        String normalizedName = name == null ? "" : name.trim();
//        if (normalizedName.isEmpty()) {
//            throw new IllegalArgumentException;
//        }
//        return normalizedName;
//    }

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

//    @Override
//    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
//        String value = p.getValueAsString();
//        if (value == null) {
//            return null;
//        }
//
//        if (value.isBlank()) {
//            return null;
//        }
//
//        return value;
//    }
}
