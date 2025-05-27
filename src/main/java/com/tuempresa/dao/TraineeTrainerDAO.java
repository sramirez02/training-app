package com.tuempresa.dao;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tuempresa.entity.TraineeTrainer;

public interface TraineeTrainerDAO extends CrudRepository<TraineeTrainer, Long> {
	List<TraineeTrainer> findByTraineeId(Long traineeId);

	List<TraineeTrainer> findByTrainerId(Long trainerId);

	@Query("SELECT tt.trainer_id FROM trainee_trainer tt JOIN trainee t ON tt.trainee_id = t.id JOIN app_user u ON t.user_id = u.id WHERE u.username = :username")
	List<Long> findTrainerIdsByTraineeUsername(@Param("username") String username);

	void deleteByTraineeId(Long traineeId);

}