package com.codexsoft.servicesupport.main.repository.impl;

import com.codexsoft.servicesupport.main.domain.Category;
import com.codexsoft.servicesupport.main.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository("categoryRepository")
public class CategoryRepositoryImpl extends BaseRepositoryImpl<Category> implements CategoryRepository {

    protected CategoryRepositoryImpl() {
        super(Category.class);
    }

    @Override
    public Category findByName(String name)
    {
        CriteriaBuilder builder = getBuilder();
        CriteriaQuery<Category> criteriaQuery = builder.createQuery(Category.class);

        Root<Category> root = criteriaQuery.from(Category.class);

        criteriaQuery.select(root).where(
                builder.equal(root.get(Category_.name), name)
        );

        return getEntityManager().createQuery(criteriaQuery).getSingleResult();
    }
}
