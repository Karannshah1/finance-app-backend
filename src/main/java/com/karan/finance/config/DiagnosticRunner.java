package com.karan.finance.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DiagnosticRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticRunner.class);

    // Injecting the values from the environment/properties file
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Override
    public void run(String... args) throws Exception {
        logger.info("============================================================");
        logger.info("DIAGNOSTIC RUNNER - DATABASE CONNECTION PROPERTIES");
        logger.info("Datasource URL: {}", dbUrl);
        logger.info("Datasource Username: {}", dbUsername);
        logger.info("============================================================");
    }
}