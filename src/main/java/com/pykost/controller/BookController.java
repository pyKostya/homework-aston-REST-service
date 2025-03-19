package com.pykost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pykost.dto.BookDTO;
import com.pykost.service.BookServiceImpl;
import com.pykost.util.ServletsUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/book/*")
public class BookController extends HttpServlet {
    private final transient BookServiceImpl bookService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BookController() {
        this.bookService = new BookServiceImpl();
    }

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                List<BookDTO> allBooks = bookService.getAll();

                resp.setContentType(ServletsUtil.CONTENT_TYPE);
                resp.setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);
                resp.getWriter().write(objectMapper.writeValueAsString(allBooks));
            } else {
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                Optional<BookDTO> bookDTO = bookService.getById(id);

                if (bookDTO.isPresent()) {
                    resp.setContentType(ServletsUtil.CONTENT_TYPE);
                    resp.setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);
                    resp.getWriter().write(objectMapper.writeValueAsString(bookDTO.get()));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (NumberFormatException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            BookDTO bookDTO = objectMapper.readValue(ServletsUtil.takeBody(req), BookDTO.class);
            BookDTO createdBook = bookService.create(bookDTO);

            resp.setContentType(ServletsUtil.CONTENT_TYPE);
            resp.setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(createdBook));
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            Long id = Long.parseLong(pathInfo.split("/")[1]);

            BookDTO bookDTO = objectMapper.readValue(ServletsUtil.takeBody(req), BookDTO.class);
            boolean updateResult = bookService.update(id, bookDTO);
            resp.setStatus(updateResult ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            final Long bookId = Long.parseLong(pathInfo.split("/")[1]);
            boolean deleteResult = bookService.delete(bookId);
            resp.setStatus(deleteResult ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
