package com.karol.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.karol")
public class AppConfig {

    public static class Profiles {
        public static final String MYSQL = "mysql";
        public static final String H2_LOCALHOST = "h2-localhost";
        public static final String H2_IN_MEMORY = "h2-in-memory";
    }
}
