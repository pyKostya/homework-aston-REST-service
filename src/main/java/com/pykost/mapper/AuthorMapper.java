package com.pykost.mapper;

import com.pykost.dto.AuthorDTO;
import com.pykost.dto.BookDTO;
import com.pykost.entity.AuthorEntity;
import com.pykost.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = BookMapper.class)
public interface AuthorMapper {

    @Mapping(source = "books", target = "books")
    AuthorEntity toEntity(AuthorDTO authorDTO);

    @Mapping(source = "books", target = "books")
    AuthorDTO toDTO(AuthorEntity author);

    List<BookDTO> mapBooks(List<BookEntity> books);
}
