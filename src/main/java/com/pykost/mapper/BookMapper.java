package com.pykost.mapper;

import com.pykost.dto.BookDTO;
import com.pykost.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    BookDTO toDTO(Book book);

    @Mapping(source = "authorId", target = "author.id")
    Book toEntity(BookDTO bookDTO);
}
