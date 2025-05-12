package com.tuempresa.service;

import com.tuempresa.entity.Training;
import java.util.Date;
import java.util.List;


public interface TrainingService {

    List<Training> getTrainingsByCriteria(String criteria1, Date startDate, Date endDate, String criteria2, String criteria3);

    List<Training> getTrainingsByTrainerCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName);

    void addTraining(Training training);

}