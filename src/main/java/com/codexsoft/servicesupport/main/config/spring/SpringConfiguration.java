package com.codexsoft.servicesupport.main.config.spring;


import com.codexsoft.servicesupport.main.ApplicationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import liquibase.integration.spring.SpringLiquibase;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Client;

import static com.codexsoft.servicesupport.main.config.spring.QualifierNames.DW_CONFIGURATION_BEAN;

@Configuration
@ComponentScan(basePackages = {"com.codexsoft.servicesupport.main"})
public class SpringConfiguration {

    private final ApplicationConfiguration configuration;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SpringConfiguration(@Qualifier(DW_CONFIGURATION_BEAN) ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }


    @Bean
    public ResourceBundleMessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.setBasenames("messages");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        return source;
    }


    @Bean
    @Autowired
    public SpringLiquibase springLiquibase(HikariDataSource hikariDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDropFirst(false);
        liquibase.setDataSource(hikariDataSource);
        liquibase.setDefaultSchema(hikariDataSource.getSchema());
        liquibase.setChangeLog(configuration.getMigration().getChangelogClassPath());
        return liquibase;
    }


    @Bean
    @Autowired
    @Qualifier(QualifierNames.DW_ENV_BEAN)
    public Client client(Environment environment) {
        var jerseyConfiguration = new JerseyClientConfiguration();
        jerseyConfiguration.setTimeout(Duration.seconds(35L));
        jerseyConfiguration.setConnectionTimeout(Duration.seconds(35L));
        jerseyConfiguration.setKeepAlive(Duration.seconds(5L));
        jerseyConfiguration.setChunkedEncodingEnabled(false);

        Client client = new JerseyClientBuilder(environment)
                .using(jerseyConfiguration)
                .build("jersey-client");

        client.register(MultiPartFeature.class);

        return client;
    }


    @Bean
    @Autowired
    @Qualifier(QualifierNames.DW_ENV_BEAN)
    public ObjectMapper objectMapper(Environment environment) {
        return environment.getObjectMapper();
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(entityManager);
//    }

    @Bean
    public EntityManager entityManager() {
        return entityManager;
    }


}
