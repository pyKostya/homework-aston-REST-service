package com.pykost.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorEntity {
    private Long id;
    private String name;
    private List<BookEntity> books;

    public AuthorEntity() {
        this.books = new ArrayList<>();
    }

    public AuthorEntity(Long id, String name) {
        this.id = id;
        this.name = name;
        this.books = new ArrayList<>();
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

    public List<BookEntity> getBooks() {
        return books;
    }

    public void setBooks(List<BookEntity> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorEntity authorEntity = (AuthorEntity) o;
        return Objects.equals(id, authorEntity.id) && Objects.equals(name, authorEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
