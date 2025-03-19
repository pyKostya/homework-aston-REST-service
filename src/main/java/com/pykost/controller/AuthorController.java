package com.pykost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pykost.dto.AuthorDTO;
import com.pykost.service.AuthorServiceImpl;
import com.pykost.util.ServletsUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/author/*")
public class AuthorController extends HttpServlet {
    private final transient AuthorServiceImpl authorService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthorController() {
        this.authorService = new AuthorServiceImpl();
    }

    public AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                List<AuthorDTO> allAuthors = authorService.getAll();

                resp.setContentType(ServletsUtil.CONTENT_TYPE);
                resp.setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);
                resp.getWriter().write(objectMapper.writeValueAsString(allAuthors));
            } else {
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                Optional<AuthorDTO> authorDTO = authorService.getById(id);

                if (authorDTO.isPresent()) {
                    resp.setContentType(ServletsUtil.CONTENT_TYPE);
                    resp.setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);
                    resp.getWriter().write(objectMapper.writeValueAsString(authorDTO.get()));
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
            AuthorDTO authorDTO = objectMapper.readValue(ServletsUtil.takeBody(req), AuthorDTO.class);
            AuthorDTO createdAuthor = authorService.create(authorDTO);

            resp.setContentType(ServletsUtil.CONTENT_TYPE);
            resp.setCharacterEncoding(ServletsUtil.CHARACTER_ENCODING);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(createdAuthor));
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            Long id = Long.parseLong(pathInfo.split("/")[1]);

            AuthorDTO authorDTO = objectMapper.readValue(ServletsUtil.takeBody(req), AuthorDTO.class);
            boolean updateResult = authorService.update(id, authorDTO);
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
            Long authorId = Long.parseLong(pathInfo.split("/")[1]);
            boolean deleteResult = authorService.delete(authorId);
            resp.setStatus(deleteResult ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
