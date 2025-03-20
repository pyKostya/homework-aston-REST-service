package com.pykost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pykost.dto.AuthorDTO;
import com.pykost.service.AuthorServiceImpl;
import com.pykost.util.ServletsUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthorControllerTest {
    @Mock
    private AuthorServiceImpl service;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private AuthorController controller;
    private StringWriter stringWriter;
    private PrintWriter writer;
    private ObjectMapper objectMapper;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() throws IOException {
        openMocks(this);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        doReturn(writer).when(response).getWriter();

        objectMapper = new ObjectMapper();

        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Author");
    }

    @Nested
    @DisplayName("Author tests for the get request")
    class GetAuthorTestEntity {
        @Test
        void doGetAllAuthorsSuccess() throws IOException {
            doReturn("/").when(request).getPathInfo();

            List<AuthorDTO> dtoList = List.of(authorDTO);

            doReturn(dtoList).when(service).getAll();

            controller.doGet(request, response);

            verify(response).setContentType(ServletsUtil.CONTENT_TYPE);
            verify(response).setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);

            writer.flush();
            String responseJson = stringWriter.toString();
            List<AuthorDTO> list = objectMapper.readValue(responseJson, List.class);

            assertThat(list).hasSize(1);

        }

        @Test
        void doGetAuthorById() throws IOException {
            doReturn("/1").when(request).getPathInfo();

            doReturn(Optional.of(authorDTO)).when(service).getById(1L);
            controller.doGet(request, response);

            verify(response).setContentType(ServletsUtil.CONTENT_TYPE);
            verify(response).setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);

            writer.flush();
            String responseJson = stringWriter.toString();
            AuthorDTO responseAuthor = objectMapper.readValue(responseJson, AuthorDTO.class);

            assertThat(authorDTO.getId()).isEqualTo(responseAuthor.getId());
            assertThat(authorDTO.getName()).isEqualTo(responseAuthor.getName());
        }

        @Test
        void doGetAuthorNotFound()  {
            doReturn("/1").when(request).getPathInfo();

            doThrow(new IllegalArgumentException("Author not found")).when(service).getById(1L);

            controller.doGet(request, response);

            verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        @Test
        void doGetAuthorInvalidPath() {
            doReturn("/invalid/path").when(request).getPathInfo();

            controller.doGet(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPostAuthor() throws IOException {
        AuthorDTO dto = new AuthorDTO();
        dto.setName("Author");
        String json = objectMapper.writeValueAsString(dto);

        doReturn(new BufferedReader(new StringReader(json))).when(request).getReader();

        doReturn(authorDTO).when(service).create(dto);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).setContentType(ServletsUtil.CONTENT_TYPE);

        writer.flush();
        String responseJson = stringWriter.toString();
        AuthorDTO responseAuthor = objectMapper.readValue(responseJson, AuthorDTO.class);

        assertThat(responseAuthor.getId()).isEqualTo(authorDTO.getId());
        assertThat(responseAuthor.getName()).isEqualTo(authorDTO.getName());
    }

    @Nested
    @DisplayName("Author tests for the put request")
    class TestAuthorPutEntity {
        @Test
        void doPutAuthorSuccess() throws IOException {
            String json = objectMapper.writeValueAsString(authorDTO);

            doReturn(new BufferedReader(new StringReader(json))).when(request).getReader();
            doReturn("/1").when(request).getPathInfo();
            doReturn(true).when(service).update(1L, authorDTO);

            controller.doPut(request, response);

            verify(response).setStatus(HttpServletResponse.SC_OK);
        }

        @Test
        void doPutAuthorNotFound() throws IOException {
            String json = objectMapper.writeValueAsString(authorDTO);

            doReturn(new BufferedReader(new StringReader(json))).when(request).getReader();
            doReturn("/2").when(request).getPathInfo();
            doReturn(false).when(service).update(2L, authorDTO);

            controller.doPut(request, response);

            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    @Nested
    @DisplayName("Author tests for the delete request")
    class TestAuthorDeleteEntity {
        @Test
        void doDeleteAuthorSuccess() {
            doReturn("/1").when(request).getPathInfo();
            doReturn(true).when(service).delete(1L);

            controller.doDelete(request, response);

            verify(service).delete(1L);
            verify(response).setStatus(HttpServletResponse.SC_OK);
        }

        @Test
        void doDeleteAuthorNotFound() {
            doReturn("/10").when(request).getPathInfo();
            doReturn(false).when(service).delete(10L);

            controller.doDelete(request, response);

            verify(service).delete(10L);
            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}