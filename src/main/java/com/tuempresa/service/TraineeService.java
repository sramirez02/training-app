
package com.tuempresa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.stereotype.Service;

import com.tuempresa.dao.TraineeDAO;
import com.tuempresa.entity.Trainee;
import com.tuempresa.entity.Trainer;
import com.tuempresa.entity.User;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TraineeService {
    private final TraineeDAO traineeDAO;
    private final UserService userService;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO, UserService userService) {
        this.traineeDAO = traineeDAO;
        this.userService = userService;
    }
    
    public Trainee getByUsername(String username) {
        User user = userService.getByUsername(username);
        return traineeDAO.findByUserId(user.getId());
    }
    
    public Trainee createUserTrainee(User user, String dateOfB, String adress) {
    	User savedUser = userService.createUser(user);
    	LocalDate date = LocalDate.parse(dateOfB);
    	Trainee trainee = new Trainee(null, savedUser.getId(), date, adress);
    	return traineeDAO.save(trainee);
    }
    
    public User getTraineeUserByUsername(String username) {
		User user = userService.getByUsername(username);
		Trainee trainee = this.getByUserId(user.getId());
		user.setTrainee(trainee);
		return user;
	}
    
    
    
	public void updateTraineeProfile(String username, String newFirstName, String newLastName, LocalDate newDateOfBirth, String newAddress) {
	    Optional<User> userOpt = userService.findByUsername(username);

	    if (userOpt.isPresent()) {
	        User user = userOpt.get();
	        user.setFirstName(newFirstName);
	        user.setLastName(newLastName);
	        user.setUsername(newFirstName.toLowerCase() + "." + newLastName.toLowerCase());
	        userService.save(user); 
	        
	        Trainee trainee = this.getByUserId(user.getId());
	        trainee.setDateOfBirth(newDateOfBirth);
	        trainee.setAddress(newAddress);
	        traineeDAO.save(trainee); 

	        	       
	        log.info("Trainee profile updated.");
	    } else {
	        log.warn("Trainee not found with username: {}", username);
	    }
	}
    
	
	public void toggleTraineeStatus(String username, boolean activate) {
	    Optional<User> userOpt = userService.findByUsername(username);

	    if (userOpt.isPresent()) {
	        User user = userOpt.get();

	        if (activate && Boolean.TRUE.equals(user.getIsActive())) {
	            log.warn("Trainee is already active.");
	        } else if (!activate && Boolean.FALSE.equals(user.getIsActive())) {
	            log.warn("Trainee is already inactive.");
	        } else {
	            user.setIsActive(activate);
	            userService.save(user);
	            log.info("Trainee {} successfully.", activate ? "activated" : "deactivated");
	        }

	    } else {
	        log.warn("Trainee not found with username: {}", username);
	    }
	}

    
    
    public Trainee createTrainee(Trainee trainee) {
    	traineeDAO.save(trainee);
        return trainee;
    }

    public Trainee getTrainee(Long id) {
        return traineeDAO.findById(id).orElse(null);
    }

    public List<Trainee> getAllTrainees() {
        return (List<Trainee>) traineeDAO.findAll();
    }
    
    public Trainee getByUserId(Long userId) {
        return traineeDAO.findByUserId(userId);
    }

    public void deleteTraineeByUsername(String username) {
        
        User user = userService.getByUsername(username);
        if (user != null) {
           
            Trainee trainee = this.getByUserId(user.getId());
            if (trainee != null) {
                
                traineeDAO.delete(trainee);
                log.info("Trainee profile and related trainings deleted successfully for username: {}", username);
            } else {
                log.warn("Trainee not found for username: {}", username);
            }
        } else {
            log.warn("User not found with username: {}", username);
        }
    }

}