package com.codexsoft.servicesupport.main.config.persistence;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
@FilterMapping
public class EntityManagerFilter implements Filter {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public EntityManagerFilter(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean participate = false;
        try {

            if (TransactionSynchronizationManager.hasResource(entityManagerFactory)) {
                participate = true;
            }

            EntityManager em = entityManagerFactory.createEntityManager();
            EntityManagerHolder emHolder = new EntityManagerHolder(em);
            TransactionSynchronizationManager.bindResource(entityManagerFactory, emHolder);

            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            if (!participate) {
                EntityManagerHolder emHolder = (EntityManagerHolder)
                        TransactionSynchronizationManager.unbindResource(entityManagerFactory);
                EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
            }
        }
    }

    @Override
    public void destroy() {

    }
}
