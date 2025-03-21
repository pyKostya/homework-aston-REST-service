package com.pykost.mapper;

import com.pykost.dto.AuthorForBookDTO;
import com.pykost.entity.AuthorEntity;
import org.mapstruct.Mapper;

@Mapper(uses = BookMapper.class)
public interface AuthorForBookMapper {
    AuthorEntity toEntity(AuthorForBookDTO authorDTO);

    AuthorForBookDTO toDTO(AuthorEntity author);
}
