package com.tuempresa.dao;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tuempresa.entity.Trainee;


@Repository
public interface TraineeDAO extends CrudRepository<Trainee, Long> {
	    
	    Trainee findByUserId(Long userId);
	    
	    @Query("SELECT t.* FROM trainee t JOIN app_user u ON t.user_id = u.id WHERE u.username = :username")
	    Optional<Trainee> findByUserUsername(@Param("username") String username);

}