package com.tuempresa.dao;

import java.util.Date;
import java.util.List;



import com.tuempresa.entity.Training;

public interface TrainingDAO  {
    List<Training> findTrainingsByCriteria(String username, Date fromDate, Date toDate, String trainerName, String trainingType);

    List<Training> findTrainingsByTrainerCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName);

    void addTraining(Training training);

} 