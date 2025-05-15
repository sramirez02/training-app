package com.tuempresa.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tuempresa.entity.TraineeTrainer;

public interface TraineeTrainerDAO extends CrudRepository<TraineeTrainer, Long> {
    List<TraineeTrainer> findByTraineeId(Long traineeId);
}