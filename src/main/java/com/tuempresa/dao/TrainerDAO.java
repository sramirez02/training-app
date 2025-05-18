package com.tuempresa.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tuempresa.entity.Trainer;

@Repository
public interface TrainerDAO extends CrudRepository<Trainer, Long> {
	Optional<Trainer> findByUserId(Long userid);

@Query("SELECT t.* FROM trainer t JOIN app_user u ON t.user_id = u.id WHERE u.is_active = true")
	List<Trainer> findAllActiveTrainers();

}

//public interface TrainerDAO extends CrudRepository<Trainer, Long> {
//	 Trainer findByUserId(long userId);
//
//	 
//}