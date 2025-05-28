package com.tuempresa.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TrainingMetrics {

    // Contadores para métricas de login
    private final Counter trainerLoginSuccessCounter;
    private final Counter trainerLoginFailureCounter;
    private final Counter traineeLoginSuccessCounter;
    private final Counter traineeLoginFailureCounter;
    
    // Métricas para operaciones CRUD
    private final Counter trainingCreatedCounter;
    private final Counter trainingDeletedCounter;

    public TrainingMetrics(MeterRegistry registry) {
        // Inicializar todas las métricas
        trainerLoginSuccessCounter = Counter.builder("training.trainer.login.success")
                .description("Número de logins exitosos de entrenadores")
                .tag("role", "trainer")  // Agrega tags para mejor filtrado
                .register(registry);
        
        trainerLoginFailureCounter = Counter.builder("training.trainer.login.failure")
                .description("Número de logins fallidos de entrenadores")
                .tag("role", "trainer")
                .register(registry);
        
        traineeLoginSuccessCounter = Counter.builder("training.trainee.login.success")
                .description("Número de logins exitosos de aprendices")
                .tag("role", "trainee")
                .register(registry);
        
        traineeLoginFailureCounter = Counter.builder("training.trainee.login.failure")
                .description("Número de logins fallidos de aprendices")
                .tag("role", "trainee")
                .register(registry);
        
        trainingCreatedCounter = Counter.builder("training.operations.create")
                .description("Número de entrenamientos creados")
                .tag("operation", "create")
                .register(registry);
        
        trainingDeletedCounter = Counter.builder("training.operations.delete")
                .description("Número de entrenamientos eliminados")
                .tag("operation", "delete")
                .register(registry);
    }

    // Métodos para incrementar contadores
    public void incrementTrainerLoginSuccess() {
        trainerLoginSuccessCounter.increment();
        log.info("Trainer login success incremented");  // Agrega logging
    }

    public void incrementTrainerLoginFailure() {
        trainerLoginFailureCounter.increment();
        log.warn("Trainer login failure incremented");
    }

    public void incrementTraineeLoginSuccess() {
        traineeLoginSuccessCounter.increment();
        log.info("Trainee login success incremented");
    }

    public void incrementTraineeLoginFailure() {
        traineeLoginFailureCounter.increment();
        log.warn("Trainee login failure incremented");
    }
    
    public void incrementTrainingCreated() {
        trainingCreatedCounter.increment();
        log.info("New training created");
    }
    
    public void incrementTrainingDeleted() {
        trainingDeletedCounter.increment();
        log.info("Training deleted");
    }
}