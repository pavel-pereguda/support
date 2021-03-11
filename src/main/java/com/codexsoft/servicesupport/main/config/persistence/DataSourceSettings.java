package com.codexsoft.servicesupport.main.config.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static com.codexsoft.servicesupport.main.config.spring.QualifierNames.DATA_SOURCE_SETTINGS_BEAN;

@Data
@NoArgsConstructor
public class DataSourceSettings implements Serializable {

    private String username;

    private String password;

    private String scheme;

    private String driverName;

    private String jdbcUrl;

    @JsonProperty("cPool")
    private ConnectionPoolSettings poolSettings = new ConnectionPoolSettings();


    @NoArgsConstructor
    @Getter
    @Setter
    public static class ConnectionPoolSettings implements Serializable {
        private String testQuery;
        private String poolName;
        private int maxSize;
        private long idleTimeout;
        private int minIdle;
        private long maxLifeTime;
        private long connectionTimeout;
    }


}
