package com.codexsoft.servicesupport;

import com.codexsoft.servicesupport.main.ApplicationConfiguration;
import com.codexsoft.servicesupport.main.SpringContextBundle;
import de.fundstech.dropwizard.auth.domain.FundswareUser;
import de.fundsware.dropwizard.swagger.SwaggerBundle;
import de.fundsware.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServiceSupportApplication extends Application<ApplicationConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ServiceSupportApplication().run(args);
    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws Exception {
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new JsonProcessingExceptionMapper(true));
        environment.jersey().register(MultiPartBundle.class);
        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(FundswareUser.class));
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor())
        );
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new SpringContextBundle<>(buildContext()));
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new SwaggerBundle<>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ApplicationConfiguration config){
                return config.getSwaggerBundleConfiguration();
            }
        });
    }

    private AnnotationConfigApplicationContext buildContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setAllowCircularReferences(false);
        context.scan("com.codexsoft.servicesupport.main.spring");
        return context;
    }
}
