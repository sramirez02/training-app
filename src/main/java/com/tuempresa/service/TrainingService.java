package com.tuempresa.service;

import com.tuempresa.dto.AddTrainingRequestDTO;
import com.tuempresa.dto.TrainerTrainingResponseDTO;
import com.tuempresa.dto.TrainingResponseDTO;
import com.tuempresa.entity.Training;
import java.util.Date;
import java.util.List;


public interface TrainingService {

    List<Training> getTrainingsByCriteria(String criteria1, Date startDate, Date endDate, String criteria2, String criteria3);

    List<Training> getTrainingsByTrainerCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName);

    void addTraining(Training training);
    
    List<TrainingResponseDTO> getTraineeTrainings(String username, Date periodFrom, Date periodTo, 
            String trainerName, String trainingType);
    
    List<TrainerTrainingResponseDTO> getTrainerTrainings(String username, Date periodFrom, 
            Date periodTo, String traineeName);
    
    void addTraining(AddTrainingRequestDTO trainingRequest);
    
    
}
