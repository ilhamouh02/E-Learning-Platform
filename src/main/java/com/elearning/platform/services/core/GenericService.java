package com.elearning.platform.services.core;

import java.util.List;

/**
 * GenericService - Interface générique pour tous les services
 * Chemin: src/main/java/com/elearning/platform/services/core/GenericService.java
 * 
 * Définit les opérations CRUD communes
 * @param <D> DTO (Data Transfer Object)
 * @param <E> Entity (Entité JPA)
 */
public interface GenericService<D, E> {
    
    /**
     * Crée une nouvelle entité
     */
    E save(D dto);
    
    /**
     * Récupère toutes les entités
     */
    List<E> getAll();
    
    /**
     * Récupère une entité par ID
     */
    E findById(Long id);
    
    /**
     * Supprime une entité par ID
     */
    void deleteById(Long id);
    
    /**
     * Met à jour une entité
     */
    E update(D dto, Long id);
}