package com.tuempresa.dao;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tuempresa.entity.Trainer;

@Repository
public interface TrainerDAO extends CrudRepository<Trainer, Long> {
	 Trainer findByUserId(long userId);

	 
}