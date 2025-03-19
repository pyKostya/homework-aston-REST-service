package com.pykost.service;

import java.util.List;
import java.util.Optional;

public interface Service<E, K> {
    E create(E e);

    Optional<E> getById(K k);

    boolean delete(K k);

    boolean update(K k, E e);

    List<E> getAll();

}
