package com.pykost.service;

import com.pykost.dao.BookDAO;
import com.pykost.dto.BookDTO;
import com.pykost.entity.Book;
import com.pykost.mapper.BookMapper;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements Service<BookDTO, Long> {
    private final BookDAO bookDAO;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookDAO bookDAO, BookMapper bookMapper) {
        this.bookDAO = bookDAO;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDTO create(BookDTO bookDTO) {
        Book entity = bookMapper.toEntity(bookDTO);
        Book save = bookDAO.save(entity);
        return bookMapper.toDTO(save);
    }

    @Override
    public Optional<BookDTO> getById(Long id) {
        return bookDAO.findById(id).map(bookMapper::toDTO);
    }

    @Override
    public boolean delete(Long id) {
        return bookDAO.delete(id);
    }

    @Override
    public void update(Long id, BookDTO bookDTO) {
        Book entity = bookMapper.toEntity(bookDTO);
        entity.setId(id);
        bookDAO.update(entity);
    }

    @Override
    public List<BookDTO> getAll() {
        return bookDAO.getAllEntity().stream()
                .map(bookMapper::toDTO)
                .toList();
    }
}
