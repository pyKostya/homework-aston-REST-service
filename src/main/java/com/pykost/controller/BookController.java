package com.pykost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pykost.dao.BookDAO;
import com.pykost.dto.BookDTO;
import com.pykost.mapper.BookMapper;
import com.pykost.mapper.BookMapperImpl;
import com.pykost.service.BookServiceImpl;
import com.pykost.util.HikariCPDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/book/*")
public class BookController extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    private final DataSource dataSource = HikariCPDataSource.getDataSource();
    private final BookDAO bookDAO = new BookDAO(dataSource);
    private final BookMapper bookMapper = new BookMapperImpl();
    private final BookServiceImpl bookService = new BookServiceImpl(bookDAO, bookMapper);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<BookDTO> allBooks = bookService.getAll();

            resp.setContentType(CONTENT_TYPE);
            resp.getWriter().write(objectMapper.writeValueAsString(allBooks));
        } else {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            Optional<BookDTO> bookDTO = bookService.getById(id);

            if (bookDTO.isPresent()) {
                resp.setContentType(CONTENT_TYPE);
                resp.getWriter().write(objectMapper.writeValueAsString(bookDTO.get()));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookDTO bookDTO = objectMapper.readValue(req.getInputStream(), BookDTO.class);
        BookDTO createdBook = bookService.create(bookDTO);

        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(objectMapper.writeValueAsString(createdBook));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Long id = Long.parseLong(pathInfo.split("/")[1]);
        BookDTO bookDTO = objectMapper.readValue(req.getInputStream(), BookDTO.class);
        bookService.update(id, bookDTO);

        resp.setContentType(CONTENT_TYPE);
        resp.getWriter().write(objectMapper.writeValueAsString(bookDTO));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Long id = Long.parseLong(pathInfo.split("/")[1]);

        if (bookService.delete(id)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
