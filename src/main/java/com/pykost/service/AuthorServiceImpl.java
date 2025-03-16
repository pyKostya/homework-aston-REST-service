package com.pykost.service;

import com.pykost.dao.AuthorDAO;
import com.pykost.dto.AuthorDTO;
import com.pykost.entity.Author;
import com.pykost.mapper.AuthorMapper;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class AuthorServiceImpl implements Service<AuthorDTO, Long>, Serializable {
    @Serial
    private static final long serialVersionUID = 1345634634L;
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

    @Override
    public void update(Long id, AuthorDTO authorDTO) {
        Author entity = AuthorMapper.INSTANCE.toEntity(authorDTO);
        entity.setId(id);
        authorDAO.update(entity);
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorDAO.getAllAuthors().stream()
                .map(AuthorMapper.INSTANCE::toDTO)
                .toList();
    }

}
