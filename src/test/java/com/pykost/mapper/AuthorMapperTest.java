package com.pykost.mapper;

import com.pykost.dto.AuthorDTO;
import com.pykost.entity.AuthorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class AuthorMapperTest {
    private AuthorMapper authorMapper;
    private AuthorEntity author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();

        author = new AuthorEntity(1L, "Author");

        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Author");
    }

    @Test
    void toEntity() {
        AuthorEntity expected = authorMapper.toEntity(authorDTO);
        assertThat(author.getId()).isEqualTo(expected.getId());
        assertThat(author.getName()).isEqualTo(expected.getName());
    }

    @Test
    void toDTO() {
        AuthorDTO expected = authorMapper.toDTO(author);
        assertThat(authorDTO.getId()).isEqualTo(expected.getId());
        assertThat(authorDTO.getName()).isEqualTo(expected.getName());
    }
}