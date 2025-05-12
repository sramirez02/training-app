package com.tuempresa.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tuempresa.entity.TrainingType;


@Repository
public interface TrainingTypeDAO extends CrudRepository<TrainingType, Long> {
    Optional<TrainingType> findByTrainingTypeName(String trainingTypeName);
}

