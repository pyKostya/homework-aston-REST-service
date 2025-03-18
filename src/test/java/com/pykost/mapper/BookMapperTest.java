package com.pykost.mapper;

import com.pykost.dto.BookDTO;
import com.pykost.entity.Author;
import com.pykost.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {
    private BookMapper bookMapper;
    private Author author;
    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapperImpl();

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
    void toDTO() {
        BookDTO expected = bookMapper.toDTO(book);
        assertThat(bookDTO.getId()).isEqualTo(expected.getId());
        assertThat(bookDTO.getName()).isEqualTo(expected.getName());
        assertThat(bookDTO.getDescription()).isEqualTo(expected.getDescription());
        assertThat(bookDTO.getAuthorId()).isEqualTo(expected.getAuthorId());
    }

    @Test
    void toEntity() {
        Book expected = bookMapper.toEntity(bookDTO);
        assertThat(book.getId()).isEqualTo(expected.getId());
        assertThat(book.getName()).isEqualTo(expected.getName());
        assertThat(book.getDescription()).isEqualTo(expected.getDescription());
        assertThat(book.getAuthor().getId()).isEqualTo(expected.getAuthor().getId());

    }
}