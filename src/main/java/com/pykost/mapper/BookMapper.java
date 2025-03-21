package com.pykost.mapper;

import com.pykost.dto.BookDTO;
import com.pykost.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(uses = AuthorForBookMapper.class)
public interface BookMapper {

    BookDTO toDTO(BookEntity book);

    BookEntity toEntity(BookDTO bookDTO);
}
