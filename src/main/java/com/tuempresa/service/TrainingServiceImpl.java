package com.tuempresa.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuempresa.client.TrainerWorkloadClient;
import com.tuempresa.dao.TrainingDAO;
import com.tuempresa.dto.AddTrainingRequestDTO;
import com.tuempresa.dto.WorkloadRequest;
import com.tuempresa.entity.Training;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE; 

    private final TrainingDAO trainingDAO; 
    private final TrainerWorkloadClient TrainerworkloadClient;

    public TrainingServiceImpl(TrainingDAO trainingRepository, TrainerWorkloadClient workloadClient) {
        this.trainingDAO = trainingRepository;
        this.TrainerworkloadClient = workloadClient;
    }

    @Override
    public List<Training> getTrainingsByCriteria(String criteria1, Date startDate, Date endDate, String criteria2, String criteria3) {
        
        return Collections.emptyList();
    }

    @Override
    public List<Training> getTrainingsByTrainerCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public void addTraining(Training training) {
        
        logger.info("Guardando training local: {}", training);
        if (trainingDAO != null) {
        	trainingDAO.addTraining(training);

        } else {
            logger.warn("trainingRepository es null — revisa inyección si esperabas usarlo");
        }

        
        WorkloadRequest workload = mapTrainingToWorkload(training, "ADD");

        
        try {
            logger.info("Enviando workload al microservicio: {}", workload);
            ResponseEntity<String> resp = TrainerworkloadClient.updateWorkload(workload);
            logger.info("Respuesta workload-service: {} - {}", resp.getStatusCodeValue(), resp.getBody());
        } catch (Exception ex) {
            logger.error("Error al enviar workload al microservicio: {}", ex.getMessage(), ex);
            
        }
    }

    @Override
    public List<com.tuempresa.dto.TrainingResponseDTO> getTraineeTrainings(String username, Date periodFrom, Date periodTo, String trainerName, String trainingType) {
        return Collections.emptyList();
    }

    @Override
    public List<com.tuempresa.dto.TrainerTrainingResponseDTO> getTrainerTrainings(String username, Date periodFrom, Date periodTo, String traineeName) {
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public void addTraining(AddTrainingRequestDTO trainingRequest) {
        
        Training training = new Training();
        
        training.setTrainingName(trainingRequest.getTrainingName());
        training.setTrainingDate(trainingRequest.getTrainingDate()); 
        training.setTrainingDuration(trainingRequest.getTrainingDuration());
        
        training.setTrainerId(0L);
        training.setTraineeId(0L);
        training.setTrainingTypeId(0L);

        
        if (trainingDAO != null) {
        	trainingDAO.addTraining(training);

        }

        
        WorkloadRequest workload = mapAddDtoToWorkload(trainingRequest, "ADD");

        try {
            logger.info("Enviando workload (desde AddTrainingRequestDTO) al microservicio: {}", workload);
            ResponseEntity<String> resp = TrainerworkloadClient.updateWorkload(workload);
            logger.info("Respuesta workload-service: {} - {}", resp.getStatusCodeValue(), resp.getBody());
        } catch (Exception ex) {
            logger.error("Error al enviar workload al microservicio: {}", ex.getMessage(), ex);
        }
    }


    private WorkloadRequest mapTrainingToWorkload(Training t, String action) {
        WorkloadRequest w = new WorkloadRequest();
        
        w.setTrainerUsername(String.valueOf(t.getTrainerId()));
        w.setTrainerFirstName("N/A");
        w.setTrainerLastName("N/A");
        w.setActive(true);
        w.setTrainingDate(parseToLocalDate(t.getTrainingDate()));
        w.setTrainingDuration(t.getTrainingDuration());
        w.setActionType(action);
        return w;
    }

    private WorkloadRequest mapAddDtoToWorkload(AddTrainingRequestDTO dto, String action) {
        WorkloadRequest w = new WorkloadRequest();
        w.setTrainerUsername(dto.getTrainerUsername());
        w.setTrainerFirstName("N/A");
        w.setTrainerLastName("N/A");
        w.setActive(true);
        w.setTrainingDate(parseToLocalDate(dto.getTrainingDate()));
        w.setTrainingDuration(dto.getTrainingDuration());
        w.setActionType(action);
        return w;
    }
 
    private LocalDate parseToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return LocalDate.now();
        }
        try {
            
            return LocalDate.parse(dateStr, ISO_DATE);
        } catch (Exception ex) {
            logger.warn("No fue posible parsear la fecha '{}', uso la fecha actual. Formato esperado yyyy-MM-dd", dateStr);
            return LocalDate.now();
        }
    }
}
