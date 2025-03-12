package com.pykost.mapper;

import com.pykost.dto.BookDTO;
import com.pykost.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Book toEntity(BookDTO bookDTO);

    BookDTO toDTO(Book book);
}
