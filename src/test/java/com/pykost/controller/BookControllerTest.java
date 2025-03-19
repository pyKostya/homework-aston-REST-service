package com.pykost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pykost.dto.BookDTO;
import com.pykost.service.BookServiceImpl;
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

class BookControllerTest {
    @Mock
    private BookServiceImpl service;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private BookController controller;
    private StringWriter stringWriter;
    private PrintWriter writer;
    private ObjectMapper objectMapper;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() throws IOException {
        openMocks(this);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        doReturn(writer).when(response).getWriter();

        objectMapper = new ObjectMapper();

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setName("book");
        bookDTO.setDescription("description");
        bookDTO.setId(1L);
    }

    @Nested
    @DisplayName("Book tests for the get request")
    class GetBookTest{
        @Test
        void doGetAllBookSuccess() throws IOException {
            doReturn("/").when(request).getPathInfo();
            List<BookDTO> bookDTOList = List.of(bookDTO);

            doReturn(bookDTOList).when(service).getAll();

            controller.doGet(request, response);

            verify(response).setContentType(ServletsUtil.CONTENT_TYPE);
            verify(response).setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);

            writer.flush();
            String responseJson = stringWriter.toString();

            List<BookDTO> responseBook = objectMapper.readValue(responseJson, List.class);

            assertThat(responseBook).hasSize(1);
        }

        @Test
        void doGetBookById() throws IOException {
            doReturn("/1").when(request).getPathInfo();

            doReturn(Optional.of(bookDTO)).when(service).getById(1L);
            controller.doGet(request, response);

            verify(response).setContentType(ServletsUtil.CONTENT_TYPE);
            verify(response).setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);

            writer.flush();
            String responseJson = stringWriter.toString();
            BookDTO responseBook = objectMapper.readValue(responseJson, BookDTO.class);

            assertThat(responseBook.getId()).isEqualTo(bookDTO.getId());
            assertThat(responseBook.getName()).isEqualTo(bookDTO.getName());
            assertThat(responseBook.getDescription()).isEqualTo(bookDTO.getDescription());
            assertThat(responseBook.getAuthor()).isEqualTo(bookDTO.getAuthor());
        }

        @Test
        void doGetBookNotFound() {
            doReturn("/1").when(request).getPathInfo();

            doThrow(new IllegalArgumentException("Book not found")).when(service).getById(1L);

            controller.doGet(request, response);

            verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        @Test
        void doGetBookInvalidPath() {
            doReturn("/invalid/path").when(request).getPathInfo();

            controller.doGet(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    @Test
    void doPostBook() throws IOException {
        BookDTO dto = new BookDTO();
        dto.setName("book");
        dto.setDescription("description");
        dto.setId(1L);
        String json = objectMapper.writeValueAsString(dto);

        doReturn(new BufferedReader(new StringReader(json))).when(request).getReader();

        doReturn(bookDTO).when(service).create(dto);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).setContentType(ServletsUtil.CONTENT_TYPE);

        writer.flush();
        String responseJson = stringWriter.toString();
        BookDTO responseBook = objectMapper.readValue(responseJson, BookDTO.class);

        assertThat(responseBook.getId()).isEqualTo(bookDTO.getId());
        assertThat(responseBook.getName()).isEqualTo(bookDTO.getName());
        assertThat(responseBook.getDescription()).isEqualTo(bookDTO.getDescription());
        assertThat(responseBook.getAuthor()).isEqualTo(bookDTO.getAuthor());
    }

    @Nested
    @DisplayName("Book tests for the put request")
    class TestBookPut {
        @Test
        void doPutBookSuccess() throws IOException {
            String json = objectMapper.writeValueAsString(bookDTO);

            doReturn(new BufferedReader(new StringReader(json))).when(request).getReader();
            doReturn("/1").when(request).getPathInfo();
            doReturn(true).when(service).update(1L, bookDTO);

            controller.doPut(request, response);

            verify(response).setStatus(HttpServletResponse.SC_OK);
        }

        @Test
        void doPutBookNotFound() throws IOException {
            String json = objectMapper.writeValueAsString(bookDTO);

            doReturn(new BufferedReader(new StringReader(json))).when(request).getReader();
            doReturn("/2").when(request).getPathInfo();
            doReturn(false).when(service).update(2L, bookDTO);

            controller.doPut(request, response);

            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    @Nested
    @DisplayName("Book tests for the delete request")
    class TestBookDelete {
        @Test
        void doDeleteBookSuccess() {
            doReturn("/1").when(request).getPathInfo();
            doReturn(true).when(service).delete(1L);

            controller.doDelete(request, response);

            verify(service).delete(1L);
            verify(response).setStatus(HttpServletResponse.SC_OK);
        }

        @Test
        void doDeleteBookNotFound() {
            doReturn("/10").when(request).getPathInfo();
            doReturn(false).when(service).delete(10L);

            controller.doDelete(request, response);

            verify(service).delete(10L);
            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}