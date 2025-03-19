package com.pykost.service;

import com.pykost.dao.AuthorDAO;
import com.pykost.dto.AuthorDTO;
import com.pykost.entity.AuthorEntity;
import com.pykost.mapper.AuthorMapper;
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
class AuthorServiceImplTest {
    @Mock
    private AuthorDAO authorDAO;
    @Mock
    private AuthorMapper authorMapper;
    @InjectMocks
    private AuthorServiceImpl authorService;
    private AuthorEntity author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new AuthorEntity(1L, "Author");

        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Author");
    }

    @Test
    void create() {
        when(authorMapper.toEntity(authorDTO)).thenReturn(author);
        when(authorDAO.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.create(authorDTO);

        assertNotNull(result);
        assertEquals(authorDTO.getId(), result.getId());
        assertEquals(authorDTO.getName(), result.getName());

        verify(authorMapper, times(1)).toEntity(authorDTO);
        verify(authorDAO, times(1)).save(author);
        verify(authorMapper, times(1)).toDTO(author);
    }

    @Test
    void getById() {
        when(authorDAO.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        Optional<AuthorDTO> result = authorService.getById(1L);

        assertThat(result).isPresent();
        assertEquals(authorDTO.getId(), result.get().getId());
        assertEquals(authorDTO.getName(), result.get().getName());

        verify(authorDAO, times(1)).findById(1L);
        verify(authorMapper, times(1)).toDTO(author);
    }

    @Test
    void delete() {
        doReturn(true).when(authorDAO).delete(author.getId());
        boolean deleteResult = authorService.delete(author.getId());

        assertThat(deleteResult).isTrue();
    }

    @Test
    void update() {
        doReturn(author).when(authorMapper).toEntity(authorDTO);
        doReturn(true).when(authorDAO).update(author);
        boolean updateResult = authorService.update(author.getId(), authorDTO);

        assertThat(updateResult).isTrue();
        verify(authorMapper, times(1)).toEntity(authorDTO);
        verify(authorDAO, times(1)).update(author);
    }

    @Test
    void getAllAuthors() {
        List<AuthorEntity> authorList = List.of(author);
        List<AuthorDTO> authorDTOList = List.of(authorDTO);

        doReturn(authorList).when(authorDAO).findAll();
        doReturn(authorDTO).when(authorMapper).toDTO(author);

        List<AuthorDTO> allAuthors = authorService.getAll();

        assertThat(authorDTOList).containsAll(allAuthors);
    }
}