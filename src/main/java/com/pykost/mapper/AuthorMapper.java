package com.pykost.mapper;

import com.pykost.dto.AuthorDTO;
import com.pykost.dto.BookDTO;
import com.pykost.entity.Author;
import com.pykost.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = BookMapper.class)
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(source = "books", target = "books")
    Author toEntity(AuthorDTO authorDTO);

    @Mapping(source = "books", target = "books")
    AuthorDTO toDTO(Author author);

    List<BookDTO> mapBooks(List<Book> books);

}
