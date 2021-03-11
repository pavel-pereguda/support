package com.codexsoft.servicesupport.main;

import com.codexsoft.servicesupport.main.config.persistence.DataSourceSettings;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fundstech.dropwizard.auth.config.FundswareAuthConfigHolder;
import de.fundstech.dropwizard.auth.config.LoginServiceClientConfiguration;
import de.fundsware.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApplicationConfiguration extends Configuration implements ConfiguredBundle, FundswareAuthConfigHolder {

    private MigrationSettings migration = new MigrationSettings();

    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration = new SwaggerBundleConfiguration();

    private DataSourceSettings dataSource = new DataSourceSettings();

    @JsonProperty("loginService")
    private LoginServiceClientConfiguration loginServiceClientConfiguration = new LoginServiceClientConfiguration();

    @JsonProperty("apiConfiguration")
    private ApiConfiguration apiConfiguration = new ApiConfiguration();

    private MainSettings mail = new MainSettings();

    @Override
    @JsonIgnore
    public LoginServiceClientConfiguration getLoginServiceClientConfiguration() {
        return loginServiceClientConfiguration;
    }

    @Getter
    @Setter
    public static class MigrationSettings {
        private String changelogClassPath;
    }

    @Getter
    @Setter
    public static class ApiConfiguration implements Serializable {
        private String machineToken;
    }

    @Getter
    @Setter
    public static class MainSettings {
        private String serverUrl;
        private String sender;
        private String list;
        private boolean enable;
        private String host;
        private long port;
        private String protocol;
        private String userName;
        private String password;
        private String smtpAuth;
        private boolean smtpStarttlsEnabled;
    }
}
