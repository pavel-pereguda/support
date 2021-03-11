package com.codexsoft.servicesupport.main.repository;


import com.codexsoft.servicesupport.main.domain.base.PersistableEntity;

import java.util.Collection;
import java.util.List;

public interface BaseRepository<E extends PersistableEntity> {

    /**
     * Returns all persistent instances, including subclasses
     *
     * @return all persistence instances (an empty <code>{@link List}</code> will be returned if no matches are found)
     */
    List<E> findAll();

    /**
     * Returns the persistent instance of the given entity class with the given identity,
     * or null if there is no such persistent instance
     *
     * @param identity persistent instance primary key
     * @return a persistent instance or null
     */
    E findByIdentity(Long identity);

    /**
     * Returns all persistent instances (including subclasses) with the given identities
     *
     * @param identities <code>{@link List}</code> of identities
     * @return all persistence instances (an empty <code>{@link List}</code> will be returned if no matches are found)
     */
    List<E> findByIdentities(Collection<Long> identities);

    /**
     * Make provided instance managed and persistent if identity is null,
     * otherwise update the state of given persistent instance
     *
     * @param entity the persistent instance
     * @return the persistent instance
     */
    E saveOrUpdate(E entity);

    /**
     * Make provided instance managed and persistent
     *
     * @param entity the persistent instance
     * @return the persistent instance
     */
    E save(E entity);

    /**
     * Make provided instance managed and persistent, flushed
     *
     * @param entity the persistent instance
     * @return the persisted and flushed instance
     */
    E saveAndFlush(E entity);

    /**
     * Update the state of given persistent instance
     *
     * @param entity the persistent instance
     * @return the persistent instance
     */
    E update(E entity);

    /**
     * Deletes provided persistent instance
     *
     * @param entity the persistent instance to delete
     */
    void delete(E entity);

    /**
     * Method deletes all provided persistent instances
     *
     * @param entities the persistent instances to delete
     */
    void delete(Collection<E> entities);

    /**
     * Deletes persistent instance with given identity
     *
     * @param identity the persistent instance primary key
     */
    void deleteByIdentity(Long identity);

    /**
     * Deletes all provided persistent instances with given identities
     *
     * @param identities the persistent instances primary keys
     */
    int deleteByIdentities(Collection<Long> identities);

    /**
     * Deletes all persistent instances including subclasses
     */
    void deleteAll();

    /**
     * Re-read the state of the given entity
     *
     * @param entity the persistent object to re-read
     */
    void refresh(E entity);

    /**
     * Check if the instance is a managed entity instance belonging to the current persistence context
     *
     * @param entity persistent object
     */
    void detach(E entity);

    /**
     * Synchronize the persistence context to the underlying database
     */
    void flush();

}
