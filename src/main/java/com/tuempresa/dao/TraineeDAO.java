package com.tuempresa.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tuempresa.entity.Trainee;


@Repository
public interface TraineeDAO extends CrudRepository<Trainee, Long> {
	    
	    Trainee findByUserId(Long userId);


}