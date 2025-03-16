package com.pykost.service;

import com.pykost.dao.BookDAO;
import com.pykost.dto.BookDTO;
import com.pykost.entity.Book;
import com.pykost.mapper.BookMapper;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements Service<BookDTO, Long>, Serializable {
    @Serial
    private static final long serialVersionUID = 89766546342L;
    private final BookDAO bookDAO = BookDAO.getInstance();

    @Override
    public BookDTO create(BookDTO bookDTO) {
        Book entity = BookMapper.INSTANCE.toEntity(bookDTO);
        Book save = bookDAO.save(entity);
        return BookMapper.INSTANCE.toDTO(save);
    }

    @Override
    public Optional<BookDTO> getById(Long id) {
        return bookDAO.findById(id).map(BookMapper.INSTANCE::toDTO);
    }

    @Override
    public boolean delete(Long id) {
        return bookDAO.delete(id);
    }

    @Override
    public void update(Long id, BookDTO bookDTO) {
        Book entity = BookMapper.INSTANCE.toEntity(bookDTO);
        entity.setId(id);
        bookDAO.update(entity);
    }

    public List<BookDTO> getAllBooks() {
        return bookDAO.getAllBooks().stream()
                .map(BookMapper.INSTANCE::toDTO)
                .toList();
    }
}
