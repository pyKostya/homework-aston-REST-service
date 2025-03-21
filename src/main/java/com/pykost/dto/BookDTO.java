package com.pykost.dto;

import java.util.Objects;

public class BookDTO {
    private Long id;
    private String name;
    private String description;
    private AuthorForBookDTO author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuthorForBookDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorForBookDTO author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Objects.equals(id, bookDTO.id) && Objects.equals(name, bookDTO.name) && Objects.equals(description, bookDTO.description) && Objects.equals(author, bookDTO.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Objects.equals(id, bookDTO.id) && Objects.equals(name, bookDTO.name) && Objects.equals(description, bookDTO.description) && Objects.equals(authorId, bookDTO.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, authorId);
    }
}
