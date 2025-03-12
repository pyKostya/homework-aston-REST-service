package com.pykost.service;

import java.util.Optional;

public interface Service<E, K> {
    E create(E e);
    Optional<E> getById(K k);
    boolean delete(K k);
    void update(E e);

}
