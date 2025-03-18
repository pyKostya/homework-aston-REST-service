package com.pykost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pykost.dao.AuthorDAO;
import com.pykost.dto.AuthorDTO;
import com.pykost.mapper.AuthorMapper;
import com.pykost.mapper.AuthorMapperImpl;
import com.pykost.service.AuthorServiceImpl;
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

@WebServlet("/author/*")
public class AuthorController extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    DataSource dataSource = HikariCPDataSource.getDataSource();
    private final AuthorDAO authorDAO = new AuthorDAO(dataSource);
    private final AuthorMapper authorMapper = new AuthorMapperImpl();
    private final AuthorServiceImpl authorService = new AuthorServiceImpl(authorDAO, authorMapper);
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<AuthorDTO> allAuthors = authorService.getAll();

            resp.setContentType(CONTENT_TYPE);
            resp.getWriter().write(objectMapper.writeValueAsString(allAuthors));
        } else {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            Optional<AuthorDTO> authorDTO = authorService.getById(id);

            if (authorDTO.isPresent()) {
                resp.setContentType(CONTENT_TYPE);
                resp.getWriter().write(objectMapper.writeValueAsString(authorDTO.get()));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthorDTO authorDTO = objectMapper.readValue(req.getInputStream(), AuthorDTO.class);
        AuthorDTO createdAuthor = authorService.create(authorDTO);

        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(objectMapper.writeValueAsString(createdAuthor));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Long id = Long.parseLong(pathInfo.split("/")[1]);

        AuthorDTO authorDTO = objectMapper.readValue(req.getInputStream(), AuthorDTO.class);
        authorService.update(id, authorDTO);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Long id = Long.parseLong(pathInfo.split("/")[1]);

        if (authorService.delete(id)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

    }
}
