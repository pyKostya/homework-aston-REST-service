package com.pykost.entity;

import java.util.Objects;

public class BookEntity {
    private Long id;
    private String name;
    private String description;
    private AuthorEntity authorEntity;

    public BookEntity() {
    }

    public BookEntity(Long id, String name, String description, AuthorEntity authorEntity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.authorEntity = authorEntity;
    }

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

    public AuthorEntity getAuthor() {
        return authorEntity;
    }

    public void setAuthor(AuthorEntity authorEntity) {
        this.authorEntity = authorEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity book = (BookEntity) o;
        return Objects.equals(id, book.id) && Objects.equals(name, book.name) && Objects.equals(description, book.description) && Objects.equals(authorEntity, book.authorEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, authorEntity);
    }
}
