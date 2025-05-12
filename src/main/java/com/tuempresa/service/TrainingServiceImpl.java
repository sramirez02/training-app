package com.tuempresa.service;

import com.tuempresa.dao.TrainingDAO;
import com.tuempresa.entity.Training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingServiceImpl(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public List<Training> getTrainingsByCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return trainingDAO.findTrainingsByCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    @Override
    public List<Training> getTrainingsByTrainerCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        return trainingDAO.findTrainingsByTrainerCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

    @Override
    public void addTraining(Training training) {
        trainingDAO.addTraining(training);
    }    
    
}