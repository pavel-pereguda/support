package com.codexsoft.servicesupport.main.repository.impl;

import com.codexsoft.servicesupport.main.domain.base.PersistableEntity;
import com.codexsoft.servicesupport.main.repository.BaseRepository;
import com.codexsoft.servicesupport.main.repository.util.JpaUtils;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BaseRepositoryImpl<E extends PersistableEntity> implements BaseRepository<E>
{
    protected Class<E> persistenceClass;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Set the persistent class to this object
     *
     * @param persistenceClass persistent class
     */
    protected BaseRepositoryImpl(Class<E> persistenceClass)
    {
        assert persistenceClass != null;
        this.persistenceClass = persistenceClass;
    }

    /**
     * Provides access to entityManager instance
     *
     * @return <class>{@link EntityManager}</class> object
     */
    protected EntityManager getEntityManager()
    {
        return entityManager;
    }

    /**
     * Returns <class>{@link CriteriaBuilder}</class> instance for the creation of
     * <class>{@link CriteriaQuery}</class> objects
     *
     * @return <class>{@link CriteriaQuery}</class> instance
     */
    protected CriteriaBuilder getBuilder()
    {
        return getEntityManager().getCriteriaBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<E> findAll()
    {
        CriteriaBuilder builder = getBuilder();
        CriteriaQuery<E> criteriaQuery = builder.createQuery(persistenceClass);

        Root<E> entityRoot = criteriaQuery.from(persistenceClass);

        criteriaQuery.select(entityRoot);

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public E findByIdentity(Long identity)
    {
        E entity = getEntityManager().find(persistenceClass, identity);
        if (entity == null)
        {
            throw new EmptyResultDataAccessException("Entity " + persistenceClass + " with identity=" + identity + " not found", 1);
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<E> findByIdentities(Collection<Long> identities)
    {
        if (identities.isEmpty())
        {
            return Collections.emptyList();
        }

        CriteriaBuilder builder = getBuilder();
        CriteriaQuery<E> criteriaQuery = builder.createQuery(persistenceClass);

        Root<E> root = criteriaQuery.from(persistenceClass);

        criteriaQuery.select(root).where(
                JpaUtils.in(builder, root.get(PersistableEntity.IDENTITY_ATTR), identities)
        );

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E saveOrUpdate(E entity)
    {
        if (entity.getId() == null)
        {
            return save(entity);
        }
        else
        {
            return update(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E save(E entity)
    {
        getEntityManager().persist(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E saveAndFlush(E entity)
    {
        EntityManager em = getEntityManager();
        em.persist(entity);
        em.flush();
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E update(E entity)
    {
        return getEntityManager().merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(E entity)
    {
        getEntityManager().remove(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Collection<E> entities)
    {
        deleteByIdentities(JpaUtils.getIdentities(entities));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByIdentity(Long identity)
    {
        CriteriaBuilder builder = getBuilder();
        CriteriaDelete<E> criteriaDelete = builder.createCriteriaDelete(persistenceClass);

        Root<E> root = criteriaDelete.from(persistenceClass);

        criteriaDelete.where(
                builder.equal(root.get(PersistableEntity.IDENTITY_ATTR), identity)
        );

        getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteByIdentities(Collection<Long> identities)
    {
        if (identities.isEmpty())
        {
            return 0;
        }

        CriteriaBuilder builder = getBuilder();
        CriteriaDelete<E> criteriaDelete = builder.createCriteriaDelete(persistenceClass);

        Root<E> root = criteriaDelete.from(persistenceClass);

        criteriaDelete.where(
                JpaUtils.in(builder, root.get(PersistableEntity.IDENTITY_ATTR), identities)
        );

        return getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll()
    {
        CriteriaBuilder builder = getBuilder();
        CriteriaDelete<E> criteriaDelete = builder.createCriteriaDelete(persistenceClass);

        criteriaDelete.from(persistenceClass);

        getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh(E entity)
    {
        getEntityManager().refresh(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detach(E entity)
    {
        getEntityManager().detach(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush()
    {
        getEntityManager().flush();
    }
}
