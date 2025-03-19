package com.pykost.util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ServletsUtilTest {

    @Test
    void takeBody() throws IOException {
        String requestBody = "{\"name\": \"Test\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(reader);

        String result = ServletsUtil.takeBody(request);

        assertEquals(requestBody, result);

    }
}