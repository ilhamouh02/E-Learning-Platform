package com.elearning.platform.services.core;

import java.util.List;

public interface GenericService<D, E> {
    E save(D dto);
    List<E> getAll();
    E findById(Long id);
    void deleteById(Long id);
    E update(D dto, Long id);
}