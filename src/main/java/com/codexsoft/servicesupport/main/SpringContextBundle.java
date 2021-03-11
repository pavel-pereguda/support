package com.codexsoft.servicesupport.main;

import com.codahale.metrics.health.HealthCheck;
import com.codexsoft.servicesupport.main.config.exception.ServiceServerException;
import com.codexsoft.servicesupport.main.config.persistence.FilterMapping;
import com.codexsoft.servicesupport.main.config.spring.QualifierNames;
import com.google.common.base.Preconditions;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.util.component.LifeCycle;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.EnumSet;
import java.util.Map;

import static com.codexsoft.servicesupport.main.config.spring.QualifierNames.DATA_SOURCE_SETTINGS_BEAN;
import static com.codexsoft.servicesupport.main.config.spring.QualifierNames.DW_CONFIGURATION_BEAN;

@Log4j2
public class SpringContextBundle<T extends ApplicationConfiguration> implements ConfiguredBundle<T> {

    private ConfigurableApplicationContext context;


    public SpringContextBundle(ConfigurableApplicationContext context) {
        Preconditions.checkArgument(!context.isActive(), "Context must be not active in order to register configuration or placeholder");
        this.context = context;
    }


    private void registerEnvironment(Environment environment) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton(QualifierNames.DW_ENV_BEAN, environment);
        log.info("Registered Environment  under name:{}", QualifierNames.DW_ENV_BEAN);
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {

        registerConfiguration(configuration, context);

        registerEnvironment(environment);

        if (!context.isActive()) context.refresh();

        registerManaged(environment, context);
        registerLifecycle(environment, context);
        registerTasks(environment, context);
        registerHealthChecks(environment, context);
        registerProviders(environment, context);
        registerResources(environment, context);
        registerCustomFilters(environment, context);
    }

    private void registerCustomFilters(Environment environment, ConfigurableApplicationContext context) {
        context.getBeansOfType(Filter.class)
                .forEach((beanName, bean) -> {
                    var filterMapping = bean.getClass().getAnnotation(FilterMapping.class);
                    if (filterMapping == null) {
                        throw new ServiceServerException("Filter " + beanName + " does not have @FilterMapping annotation");
                    }
                    environment.servlets()
                            .addFilter(beanName, bean)
                            .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, filterMapping.value());
                });
    }


    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // nothing doing
    }

    public ConfigurableApplicationContext getContext() {
        return context;
    }


    // ~ Dropwizard Environment initialization methods -----------------------------------------------------------------

    /**
     * Register {@link Managed}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerManaged(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Managed> managedBeanMap = context.getBeansOfType(Managed.class);

        managedBeanMap.forEach((beanName, managed) -> {
            environment.lifecycle().manage(managed);
            log.info("Registering managed: " + managed.getClass().getName());
        });
    }


    /**
     * Register {@link LifeCycle}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerLifecycle(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, LifeCycle> lifeCycleBeanMap = context.getBeansOfType(LifeCycle.class);
        lifeCycleBeanMap.forEach((beanName, lifeCycle) -> {
            environment.lifecycle().manage(lifeCycle);
            log.info("Registering lifeCycle: " + lifeCycle.getClass().getName());
        });
    }


    /**
     * Register {@link Task}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerTasks(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Task> taskBeanMap = context.getBeansOfType(Task.class);
        taskBeanMap.forEach((beanName, task) -> {
            environment.jersey().register(task);
            log.info("Registering task: " + task.getClass().getName());
        });
    }


    /**
     * Register {@link HealthCheck}s in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerHealthChecks(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, HealthCheck> healthChecksMap = context.getBeansOfType(HealthCheck.class);

        healthChecksMap.forEach((beanName, bean) -> {
            environment.healthChecks().register(beanName, bean);
            log.info("Registered healCheck :{}", beanName);
        });
    }

    /**
     * Register objects annotated with {@link Provider} in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerProviders(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Object> providerBeanMap = context.getBeansWithAnnotation(Provider.class);

        providerBeanMap.forEach((beanName, provider) -> {
            environment.jersey().register(provider);
            log.info("Registering provider : " + provider.getClass().getName());
        });

    }


    /**
     * Register resources annotated with {@link Path} in Dropwizard {@link Environment} from Spring application context.
     *
     * @param environment the Dropwizard environment
     * @param context     the Spring application context
     */
    private void registerResources(Environment environment, ConfigurableApplicationContext context) {
        final Map<String, Object> pathsBeanMap = context.getBeansWithAnnotation(Path.class);

        pathsBeanMap.forEach((beanName, resource) -> {
            environment.jersey().register(resource);
            log.info("Registering resource : " + resource.getClass().getName());
        });
    }


    /**
     * Register Dropwizard Configuration as a Bean Spring.
     *
     * @param configuration Dropwizard configuration
     * @param context       spring application context
     */
    private void registerConfiguration(T configuration, ConfigurableApplicationContext context) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton(DW_CONFIGURATION_BEAN, configuration);
        log.info("Registered Dropwizard Configuration under name : {}", DW_CONFIGURATION_BEAN);
        beanFactory.registerSingleton(DATA_SOURCE_SETTINGS_BEAN, configuration.getDataSource());
        log.info("Registered Dropwizard Data Source settings  under name : {}", DATA_SOURCE_SETTINGS_BEAN);
    }

}
