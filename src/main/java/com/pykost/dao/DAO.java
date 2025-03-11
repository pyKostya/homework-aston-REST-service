package com.pykost.dao;

import com.pykost.entity.Book;

import java.util.Optional;

public interface DAO<E, K> {
    boolean delete(K key);
    E save(E entity);
    void update(E entity);
    Optional<E> findById(K key);
}
