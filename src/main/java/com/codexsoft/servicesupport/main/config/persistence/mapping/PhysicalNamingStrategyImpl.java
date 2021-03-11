package com.codexsoft.servicesupport.main.config.persistence.mapping;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

public class PhysicalNamingStrategyImpl implements PhysicalNamingStrategy {

    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name);
    }

    private Identifier apply(Identifier name) {
        if (name == null) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));

            for (int i = 1; i < builder.length() - 1; ++i) {
                if (this.isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
                    builder.insert(i++, '_');
                }
            }

            return this.getIdentifier(builder.toString(), name.isQuoted());
        }
    }

    protected Identifier getIdentifier(String name, boolean quoted) {
        return new Identifier(name.toLowerCase(Locale.ROOT), quoted);
    }


    private boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }
}
