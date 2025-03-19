package com.pykost.mapper;

import com.pykost.dto.AuthorForBookDTO;
import com.pykost.entity.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = BookMapper.class)
public interface AuthorForBookMapper {
    @Mapping(source = "name", target = "name")
    AuthorEntity toEntity(AuthorForBookDTO authorDTO);

    @Mapping(source = "name", target = "name")
    AuthorForBookDTO toDTO(AuthorEntity author);
}
