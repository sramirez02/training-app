package com.tuempresa.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class CsvFilesHealthIndicator implements HealthIndicator {

    private final ResourceLoader resourceLoader;
    private final String[] csvFiles = {
        "classpath:trainingtypes.csv",
        "classpath:users.csv",
        "classpath:trainers.csv",
        "classpath:trainees.csv",
        "classpath:trainings.csv"
    };

    public CsvFilesHealthIndicator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Health health() {
        Health.Builder builder = Health.up();
        
        for (String filePath : csvFiles) {
            try {
                Resource resource = resourceLoader.getResource(filePath);
                if (!resource.exists()) {
                    builder.down().withDetail(filePath, "File not found");
                } else {
                    builder.withDetail(filePath, "Available");
                }
            } catch (Exception e) {
                builder.down().withDetail(filePath, "Error: " + e.getMessage());
            }
        }
        
        return builder.build();
    }
}