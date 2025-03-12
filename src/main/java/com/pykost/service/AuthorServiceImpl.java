package com.pykost.service;

import com.pykost.dao.AuthorDAO;
import com.pykost.dto.AuthorDTO;
import com.pykost.entity.Author;
import com.pykost.mapper.AuthorMapper;

import java.util.Optional;

public class AuthorServiceImpl implements Service<AuthorDTO, Long> {
    private final AuthorDAO authorDAO = AuthorDAO.getInstance();

    @Override
    public AuthorDTO create(AuthorDTO authorDTO) {
        Author entity = AuthorMapper.INSTANCE.toEntity(authorDTO);
        Author save = authorDAO.save(entity);
        return AuthorMapper.INSTANCE.toDTO(save);
    }

    @Override
    public Optional<AuthorDTO> getById(Long id) {
        return authorDAO.findById(id)
                .map(AuthorMapper.INSTANCE::toDTO);
    }

    @Override
    public boolean delete(Long id) {
        return authorDAO.delete(id);
    }

    public void update(AuthorDTO authorDTO) {
        Author entity = AuthorMapper.INSTANCE.toEntity(authorDTO);
        authorDAO.update(entity);
    }

    public Optional<AuthorDTO> findByIdAllBooks(Long id) {
        return authorDAO.findByIdAllBooks(id)
                .map(AuthorMapper.INSTANCE::toDTO);
    }

}
