package com.pykost.service;

import com.pykost.dao.AuthorDAO;
import com.pykost.dto.AuthorDTO;
import com.pykost.entity.Author;
import com.pykost.mapper.AuthorMapper;

import java.util.List;
import java.util.Optional;

public class AuthorServiceImpl implements Service<AuthorDTO, Long> {
    private final AuthorDAO authorDAO;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorDAO authorDAO, AuthorMapper authorMapper) {
        this.authorDAO = authorDAO;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorDTO create(AuthorDTO authorDTO) {
        Author entity = authorMapper.toEntity(authorDTO);
        Author save = authorDAO.save(entity);
        return authorMapper.toDTO(save);
    }

    @Override
    public Optional<AuthorDTO> getById(Long id) {
        return authorDAO.findById(id)
                .map(authorMapper::toDTO);
    }

    @Override
    public boolean delete(Long id) {
        return authorDAO.delete(id);
    }

    @Override
    public void update(Long id, AuthorDTO authorDTO) {
        Author entity = authorMapper.toEntity(authorDTO);
        entity.setId(id);
        authorDAO.update(entity);
    }

    @Override
    public List<AuthorDTO> getAll() {
        return authorDAO.getAllEntity().stream()
                .map(authorMapper::toDTO)
                .toList();
    }

}
