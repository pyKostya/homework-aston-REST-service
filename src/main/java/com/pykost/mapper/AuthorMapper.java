package com.pykost.mapper;

import com.pykost.dto.AuthorDTO;
import com.pykost.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(source = "name", target = "name")
    Author toEntity(AuthorDTO authorDTO);

    AuthorDTO toDTO(Author author);

}
