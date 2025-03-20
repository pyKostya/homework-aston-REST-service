package com.pykost.mapper;

import com.pykost.dto.AuthorDTO;
import com.pykost.entity.AuthorEntity;
import org.mapstruct.Mapper;


@Mapper(uses = BookMapper.class)
public interface AuthorMapper {

    AuthorEntity toEntity(AuthorDTO authorDTO);

    AuthorDTO toDTO(AuthorEntity authorEntity);



}
