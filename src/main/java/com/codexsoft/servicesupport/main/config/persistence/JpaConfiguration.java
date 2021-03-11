package com.codexsoft.servicesupport.main.config.persistence;

import com.codexsoft.servicesupport.main.config.persistence.mapping.ImplicitNamingStrategyImpl;
import com.codexsoft.servicesupport.main.config.persistence.mapping.PhysicalNamingStrategyImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.Properties;

import static com.codexsoft.servicesupport.main.config.spring.QualifierNames.DATA_SOURCE_SETTINGS_BEAN;

@EnableTransactionManagement
@Configuration
public class JpaConfiguration {


    @Bean
    public EntityManagerFilter entityManagerFilter(EntityManagerFactory entityManagerFactory) {
        return new EntityManagerFilter(entityManagerFactory);
    }

    @Bean
    public LocalSessionFactoryBean entityManagerFactory(HikariDataSource hikariDataSource) throws IOException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(hikariDataSource);
        sessionFactory.setPackagesToScan(
                "com.codexsoft.servicesupport.main.repository",
                "com.codexsoft.servicesupport.main.domain"
        );
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setImplicitNamingStrategy(new ImplicitNamingStrategyImpl());
        sessionFactory.setPhysicalNamingStrategy(new PhysicalNamingStrategyImpl());
        sessionFactory.afterPropertiesSet();
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(sessionFactory);
        transactionManager.setRollbackOnCommitFailure(true);
        return transactionManager;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(HikariDataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean("hikariDataSource")
    public HikariDataSource hikariDataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public HikariConfig hikariConfig(DataSourceSettings dataSourceSettings) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dataSourceSettings.getDriverName());
        hikariConfig.setJdbcUrl(dataSourceSettings.getJdbcUrl());
        hikariConfig.setUsername(dataSourceSettings.getUsername());
        hikariConfig.setPassword(dataSourceSettings.getPassword());

        var poolSettings = dataSourceSettings.getPoolSettings();

        hikariConfig.setMaximumPoolSize(poolSettings.getMaxSize());
        hikariConfig.setConnectionTestQuery(poolSettings.getTestQuery());
        hikariConfig.setPoolName(poolSettings.getPoolName());
        hikariConfig.setIdleTimeout(poolSettings.getIdleTimeout());
        hikariConfig.setMaxLifetime(poolSettings.getMaxLifeTime());
        hikariConfig.setMinimumIdle(poolSettings.getMinIdle());
        hikariConfig.setConnectionTimeout(poolSettings.getConnectionTimeout());
        return hikariConfig;
    }




    @Bean
    public JdbcTemplate jdbcTemplate(HikariDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private Properties hibernateProperties() {

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        hibernateProperties.setProperty("hibernate.ddl-auto", "none");
        hibernateProperties.setProperty("hibernate.use-new-id-generator-mappings", "true");
        return hibernateProperties;
    }

}
