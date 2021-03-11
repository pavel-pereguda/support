package com.codexsoft.servicesupport.main.repository.util;

import com.codexsoft.servicesupport.main.domain.base.PersistableEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class JpaUtils
{
    private JpaUtils()
    {
    }

    /**
     * Case insensitive like. Uses lowercase.
     * @param builder
     *            builder
     * @param property
     *            entity property
     * @param value
     *            value to check
     * @return restriction
     */
    public static Predicate ilike(CriteriaBuilder builder, Path<String> property, String value)
    {
        return builder.like(
                builder.lower(property),
                builder.lower(builder.literal(value))
        );
    }

    public static Collection<Long> getIdentities(Collection<? extends PersistableEntity> objects)
    {
        List<Long> identities;
        if (objects != null)
        {
            identities = new ArrayList<>(objects.size());
            for (PersistableEntity object : objects)
            {
                if (object != null && object.getId() != null)
                {
                    identities.add(object.getId());
                }
            }
        }
        else
        {
            identities = Collections.emptyList();
        }
        return identities;
    }

    public static Predicate in(CriteriaBuilder builder, Path<?> propertyName, Collection<?> values)
    {
        List<?> items = values instanceof List ? (List<?>) values : new ArrayList<>(values);
        Predicate disjunction = builder.disjunction();
        while (!items.isEmpty())
        {
            int count = Math.min(items.size(), 1000);
            List<?> part = items.subList(0, count);
            disjunction.getExpressions().add(propertyName.in(part));
            items = items.subList(count, items.size());
        }
        return disjunction;
    }
}
