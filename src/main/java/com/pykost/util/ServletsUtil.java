package com.pykost.util;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class ServletsUtil {
    public static final String CONTENT_TYPE = "application/json";
    public static final String CHARACTER_ENCODING = "UTF-8";

    private ServletsUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String takeBody(HttpServletRequest req) throws IOException {
        try (BufferedReader reader = req.getReader()) {
            int intValueOfChar;
            StringBuilder result = new StringBuilder();
            while ((intValueOfChar = reader.read()) != -1) {
                result.append((char) intValueOfChar);
            }
            return result.toString();
        }
    }
}
