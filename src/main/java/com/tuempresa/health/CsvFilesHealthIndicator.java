package com.tuempresa.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

//@Component
public class CsvFilesHealthIndicator implements HealthIndicator {

    @Value("${trainingtypes.file.path}")
    private String trainingTypesPath;
    
    @Value("${users.file.path}")
    private String usersPath;
    
    @Value("${trainers.file.path}")
    private String trainersPath;
    
    @Value("${trainees.file.path}")
    private String traineesPath;
    
    @Value("${trainings.file.path}")
    private String trainingsPath;

    @Override
    public Health health() {
        boolean allFilesExist = checkAllFilesExist();
        if (allFilesExist) {
            return Health.up()
                   .withDetail("trainingTypes", "Exists")
                   .withDetail("users", "Exists")
                   .withDetail("trainers", "Exists")
                   .withDetail("trainees", "Exists")
                   .withDetail("trainings", "Exists")
                   .build();
        } else {
            return Health.down()
                   .withDetail("trainingTypes", checkFile(trainingTypesPath))
                   .withDetail("users", checkFile(usersPath))
                   .withDetail("trainers", checkFile(trainersPath))
                   .withDetail("trainees", checkFile(traineesPath))
                   .withDetail("trainings", checkFile(trainingsPath))
                   .build();
        }
    }

    private boolean checkAllFilesExist() {
        return new File(trainingTypesPath).exists() &&
               new File(usersPath).exists() &&
               new File(trainersPath).exists() &&
               new File(traineesPath).exists() &&
               new File(trainingsPath).exists();
    }
    
    private String checkFile(String path) {
        return new File(path).exists() ? "Exists" : "Missing";
    }
}