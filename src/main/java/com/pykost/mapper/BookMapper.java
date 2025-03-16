package com.pykost.mapper;

import com.pykost.dto.BookDTO;
import com.pykost.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "author.id", target = "authorId")
    BookDTO toDTO(Book book);

    @Mapping(source = "authorId", target = "author.id")
    Book toEntity(BookDTO bookDTO);
}
