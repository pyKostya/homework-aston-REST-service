package com.pykost.mapper;

import com.pykost.dto.BookDTO;
import com.pykost.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    BookDTO toDTO(BookEntity book);

    @Mapping(source = "authorId", target = "author.id")
    BookEntity toEntity(BookDTO bookDTO);
}
