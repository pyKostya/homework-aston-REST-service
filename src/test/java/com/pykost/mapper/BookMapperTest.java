package com.pykost.mapper;

import com.pykost.dto.AuthorForBookDTO;
import com.pykost.dto.BookDTO;
import com.pykost.entity.AuthorEntity;
import com.pykost.entity.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {
    private BookMapper bookMapper;
    private AuthorEntity author;
    private AuthorForBookDTO authorForBookDTO;
    private BookEntity book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapperImpl();

        author = new AuthorEntity(1L, "Author");

        book = new BookEntity();
        book.setId(1L);
        book.setName("Book");
        book.setDescription("Description");
        book.setAuthor(author);

        authorForBookDTO = new AuthorForBookDTO();
        authorForBookDTO.setId(1L);
        authorForBookDTO.setName("Author");


        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setName("Book");
        bookDTO.setDescription("Description");
        bookDTO.setAuthor(authorForBookDTO);
    }

    @Test
    void toDTO() {
        BookDTO expected = bookMapper.toDTO(book);
        assertThat(bookDTO.getId()).isEqualTo(expected.getId());
        assertThat(bookDTO.getName()).isEqualTo(expected.getName());
        assertThat(bookDTO.getDescription()).isEqualTo(expected.getDescription());
        assertThat(bookDTO.getAuthor()).isEqualTo(expected.getAuthor());
    }

    @Test
    void toEntity() {
        BookEntity expected = bookMapper.toEntity(bookDTO);
        assertThat(book.getId()).isEqualTo(expected.getId());
        assertThat(book.getName()).isEqualTo(expected.getName());
        assertThat(book.getDescription()).isEqualTo(expected.getDescription());
        assertThat(book.getAuthor()).isEqualTo(expected.getAuthor());

    }
}