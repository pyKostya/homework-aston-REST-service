package com.pykost.dao;


import java.util.List;
import java.util.Optional;

public interface BaseDAO<E, K> {
    boolean delete(K key);

    E save(E entity);

    boolean update(E entity);

    Optional<E> findById(K key);

    List<E> findAll();
}
