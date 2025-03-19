package com.pykost.service;

import com.pykost.dao.BookDAO;
import com.pykost.dto.BookDTO;
import com.pykost.entity.Author;
import com.pykost.entity.Book;
import com.pykost.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookDAO bookDAO;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;
    private Author author;
    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "Author");

        book = new Book();
        book.setId(1L);
        book.setName("Book");
        book.setDescription("Description");
        book.setAuthor(author);

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setName("Book");
        bookDTO.setDescription("Description");
        bookDTO.setAuthorId(author.getId());
    }

    @Test
    void create() {
        doReturn(book).when(bookMapper).toEntity(bookDTO);
        doReturn(book).when(bookDAO).save(book);
        doReturn(bookDTO).when(bookMapper).toDTO(book);

        BookDTO expected = bookService.create(bookDTO);

        assertNotNull(expected);
        assertThat(bookDTO.getId()).isEqualTo(expected.getId());
        assertThat(bookDTO.getName()).isEqualTo(expected.getName());
        assertThat(bookDTO.getDescription()).isEqualTo(expected.getDescription());
        assertThat(bookDTO.getAuthorId()).isEqualTo(expected.getAuthorId());

        verify(bookMapper, times(1)).toEntity(bookDTO);
        verify(bookDAO, times(1)).save(book);
        verify(bookMapper, times(1)).toDTO(book);
    }

    @Test
    void getById() {
        doReturn(Optional.of(book)).when(bookDAO).findById(book.getId());
        doReturn(bookDTO).when(bookMapper).toDTO(book);

        Optional<BookDTO> expected = bookService.getById(book.getId());

        assertThat(expected).isPresent();
        assertThat(expected.get().getId()).isEqualTo(bookDTO.getId());
        assertThat(expected.get().getName()).isEqualTo(bookDTO.getName());
        assertThat(expected.get().getDescription()).isEqualTo(bookDTO.getDescription());
        assertThat(expected.get().getAuthorId()).isEqualTo(bookDTO.getAuthorId());

        verify(bookDAO, times(1)).findById(book.getId());
        verify(bookMapper, times(1)).toDTO(book);
    }

    @Test
    void delete() {
        doReturn(true).when(bookDAO).delete(book.getId());

        boolean result = bookService.delete(bookDTO.getId());

        assertThat(result).isTrue();
        verify(bookDAO, times(1)).delete(book.getId());
    }

    @Test
    void update() {
        doReturn(book).when(bookMapper).toEntity(bookDTO);
        doReturn(true).when(bookDAO).update(book);

        boolean updateResult = bookService.update(bookDTO.getId(), bookDTO);

        assertThat(updateResult).isTrue();
        verify(bookMapper, times(1)).toEntity(bookDTO);
        verify(bookDAO, times(1)).update(book);
    }

    @Test
    void getAllBooks() {
        List<Book> bookList = List.of(book);
        List<BookDTO> bookDTOList = List.of(bookDTO);

        doReturn(bookList).when(bookDAO).getAllEntity();
        doReturn(bookDTO).when(bookMapper).toDTO(book);

        List<BookDTO> result = bookService.getAll();

        assertThat(result).containsAll(bookDTOList);

        verify(bookDAO, times(1)).getAllEntity();
        verify(bookMapper, times(1)).toDTO(book);
    }
}