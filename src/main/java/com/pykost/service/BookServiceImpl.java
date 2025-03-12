package com.pykost.service;

import com.pykost.dao.BookDAO;
import com.pykost.dto.BookDTO;
import com.pykost.entity.Book;
import com.pykost.mapper.BookMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookServiceImpl implements Service<BookDTO, Long> {
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
    public void update(BookDTO bookDTO) {
        Book entity = BookMapper.INSTANCE.toEntity(bookDTO);
        bookDAO.update(entity);
    }

    public List<BookDTO> findByIdAllBooks(Long id) {
        return bookDAO.findAllBooksByAuthor(id).stream()
                .map(BookMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
}
