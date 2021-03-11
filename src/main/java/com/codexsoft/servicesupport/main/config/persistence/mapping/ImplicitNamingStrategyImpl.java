package com.codexsoft.servicesupport.main.config.persistence.mapping;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitJoinTableNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

public class ImplicitNamingStrategyImpl extends ImplicitNamingStrategyJpaCompliantImpl {

    @Override
    public Identifier determineJoinTableName(ImplicitJoinTableNameSource source) {
        String name = source.getOwningPhysicalTableName() + "_" + source.getAssociationOwningAttributePath().getProperty();
        return this.toIdentifier(name, source.getBuildingContext());
    }
}
