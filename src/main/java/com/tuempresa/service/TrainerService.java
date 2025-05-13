package com.tuempresa.service;

import com.tuempresa.dao.TrainerDAO;
import com.tuempresa.dao.UserDAO;
import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTrainerRequestDto;
import com.tuempresa.entity.Trainer;
import com.tuempresa.entity.User;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TrainerService {
	private final UserService userService;
	private final TrainerDAO trainerDAO;
	private static final Logger log = LoggerFactory.getLogger(TrainerService.class);
	
	
	
	@Autowired
	public TrainerService(UserService userService, TrainerDAO trainerDAO) {
		this.userService = userService;
		this.trainerDAO = trainerDAO;
	}

	public Trainer getTrainerByUsername(String username) {
        User user = userService.getByUsername(username);
        if (user != null) {
        	return trainerDAO.findByUserId(user.getId());
        }
        return null;
    }
	
	public User getTrainerUserByUsernameUser(String username) {
		User user = userService.getByUsername(username);
		Trainer trainer = this.getByUserId(user.getId());
		user.setTrainer(trainer);
		return user; 
	}
	
	public CreateGymUserResponseDto createUserTrainer(CreateTrainerRequestDto trainerRequestDto) {
		User userToSave = new User(trainerRequestDto.getFirstName(), trainerRequestDto.getLastName(), true);
		
		
        User savedUser = userService.createUser(userToSave);
        
        Trainer trainer = new Trainer();
        trainer.setUserId(savedUser.getId());
        trainer.setTrainingTypeId(trainerRequestDto.getTrainingTypeId());
        trainerDAO.save(trainer);
        
//        CreateGymUserResponseDto createGymUserResponseDto = new CreateGymUserResponseDto();
//        createGymUserResponseDto.setUsername(savedUser.getUsername());
//        createGymUserResponseDto.setPassword(savedUser.getPassword());
//        
//        return createGymUserResponseDto;
        return new CreateGymUserResponseDto(savedUser.getUsername(), savedUser.getPassword());
	}
	
	
	public void updateTrainerProfile(String username, String newFirstName, String newLastName, Long newTrainingTypeId) {
	    Optional<User> userOpt = userService.findByUsername(username);

	    if (userOpt.isPresent()) {
	        User user = userOpt.get();
	        user.setFirstName(newFirstName);
	        user.setLastName(newLastName);
	        user.setUsername(newFirstName.toLowerCase() + "." + newLastName.toLowerCase());

	        userService.save(user); 

	        
	        Trainer trainer = trainerDAO.findByUserId(user.getId());
	        if (trainer != null) {
	            trainer.setTrainingTypeId(newTrainingTypeId);
	            trainerDAO.save(trainer); 
	        } else {
	            log.warn("No se encontró el trainer con userId: {}", user.getId());
	        }
	       
	        log.info("Trainer profile updated.");
	    } else {
	        log.warn("Trainer not found with username: {}", username);
	    }
	}

	public void toggleTrainerStatus(String username, boolean activate) {
	    Optional<User> userOpt = userService.findByUsername(username);

	    if (userOpt.isPresent()) {
	        User user = userOpt.get();

	        if (activate && Boolean.TRUE.equals(user.getIsActive())) {
	            log.warn("Trainer is already active.");
	        } else if (!activate && Boolean.FALSE.equals(user.getIsActive())) {
	            log.warn("Trainer is already inactive.");
	        } else {
	            user.setIsActive(activate);
	            userService.save(user);
	            log.info("Trainer {} successfully.", activate ? "activated" : "deactivated");
	        }

	    } else {
	        log.warn("Trainer not found with username: {}", username);
	    }
	}
	
	
	public Trainer createTrainer(Trainer trainer) {
		return trainerDAO.save(trainer);
	}

	public Trainer getTrainer(Long id) {
		return trainerDAO.findById(id).orElse(null);
	}

	public List<Trainer> getAllTrainers() {
		return (List<Trainer>) trainerDAO.findAll();
	}

	public Trainer getByUserId(Long userId) {
		return trainerDAO.findByUserId(userId);
	}


}