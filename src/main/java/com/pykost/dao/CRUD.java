package com.pykost.dao;


import java.util.Optional;

public interface CRUD<E, K> {
    boolean delete(K key);

    E save(E entity);

    boolean update(E entity);

    Optional<E> findById(K key);
}
