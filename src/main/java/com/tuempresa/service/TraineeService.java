
package com.tuempresa.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuempresa.dao.TraineeDAO;
import com.tuempresa.dao.TraineeTrainerDAO;
import com.tuempresa.dao.TrainerDAO;
import com.tuempresa.dao.TrainingTypeDAO;
import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTraineeRequestDto;
import com.tuempresa.dto.TraineeProfileResponseDto;
import com.tuempresa.dto.TrainerInfoDto;
import com.tuempresa.dto.UpdateTraineeRequestDto;
import com.tuempresa.dto.UpdateTraineeTrainersRequest;
import com.tuempresa.entity.Trainee;
import com.tuempresa.entity.TraineeTrainer;
import com.tuempresa.entity.Trainer;
import com.tuempresa.entity.TrainingType;
import com.tuempresa.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TraineeService {
	
	
	 @Autowired
	    private TraineeDAO traineeDAO;

	    @Autowired
	    private UserService userService;

	    @Autowired
	    private TraineeTrainerDAO traineeTrainerDAO;

	    @Autowired
	    private TrainerDAO trainerDAO; 
	    
	    @Autowired
	    private TrainingTypeDAO trainingTypeDAO;

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
    
    public User getTraineeUserByUsernameUser(String username) {
		User user = userService.getByUsername(username);
		Trainee trainee = this.getByUserId(user.getId());
		user.setTrainee(trainee);
		return user;
	}
    
   
    
    public CreateGymUserResponseDto createUserTrainee(CreateTraineeRequestDto traineeRequestDto) {
		User userToSave = new User(traineeRequestDto.getFirstName(), traineeRequestDto.getLastName(), true);
		
		
        User savedUser = userService.createUser(userToSave);
        
        Trainee trainee = new Trainee();
        trainee.setUserId(savedUser.getId());
        trainee.setDateOfBirth(traineeRequestDto.getDateOfBirth());
        trainee.setAddress(traineeRequestDto.getAddress());
        traineeDAO.save(trainee);
        
        return new CreateGymUserResponseDto(savedUser.getUsername(), savedUser.getPassword());
    }
    
     
    
    public TraineeProfileResponseDto getTraineeProfile(String username) {
        User user = userService.getByUsername(username);
        if (user == null) throw new RuntimeException("Usuario no encontrado");

        Trainee trainee = traineeDAO.findByUserId(user.getId());
        if (trainee == null) {
        	throw new RuntimeException("Trainee no encontrado");
        }

        return TraineeProfileResponseDto.builder()
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .dateOfBirth(trainee.getDateOfBirth())
            .address(trainee.getAddress())
            .isActive(user.getIsActive())
            .trainersList(getTrainersForTrainee(trainee.getId()))
            .build();
    }

   public List<TrainerInfoDto> getTrainersForTrainee(Long traineeId) {
        List<TraineeTrainer> relations = traineeTrainerDAO.findByTraineeId(traineeId);
        return relations.stream()
            .map(relation -> {
                Trainer trainer = trainerDAO.findById(relation.getTrainerId())
                    .orElseThrow(() -> new RuntimeException("Trainer no encontrado"));
                User trainerUser = userService.getUserById(trainer.getUserId());
                TrainingType trainingType = trainingTypeDAO.findById(trainer.getTrainingTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de entrenamiento no encontrado"));

                return TrainerInfoDto.builder()
                    .username(trainerUser.getUsername())
                    .firstName(trainerUser.getFirstName())
                    .lastName(trainerUser.getLastName())
                    .specialization(trainingType) // Usa el objeto TrainingType completo
                    .build();
            })
            .collect(Collectors.toList());
    }

    
   
    
    public TraineeProfileResponseDto updateTraineeProfile(UpdateTraineeRequestDto request) {
        
        User user = userService.getByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado: " + request.getUsername());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setIsActive(request.isActive());

        userService.save(user);

        Trainee trainee = traineeDAO.findByUserId(user.getId());
        if (request.getDateOfBirth() != null) {
            trainee.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            trainee.setAddress(request.getAddress());
        }
        traineeDAO.save(trainee);

        return getTraineeProfile(user.getUsername());
    }
    
    public void deleteTraineeByUsername(String username) {
        User user = userService.getByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }

        Trainee trainee = traineeDAO.findByUserId(user.getId());
        if (trainee != null) {
            traineeDAO.delete(trainee); 
            log.info("Trainee eliminado: {}", username);
        } else {
            throw new RuntimeException("Trainee no encontrado para el usuario: " + username);
        }
    }
    
    public List<TrainerInfoDto> updateTraineeTrainers(UpdateTraineeTrainersRequest request) {
        
        Trainee trainee = traineeDAO.findByUserUsername(request.getTraineeUsername())
                .orElseThrow(() -> new RuntimeException("Trainee not found"));
    
        
        List<TraineeTrainer> newRelations = request.getTrainersUsernames().stream()
                .map(trainerUsername -> {
                    Trainer trainer = trainerDAO.findByUserUsername(trainerUsername)
                    		.orElseThrow(() -> new RuntimeException("Trainer not found: " + trainerUsername));
                    
                    TraineeTrainer relation = new TraineeTrainer();
                    relation.setTraineeId(trainee.getId());
                    relation.setTrainerId(trainer.getId());
                    return relation;
                })
                .collect(Collectors.toList());
        
        traineeTrainerDAO.saveAll(newRelations);
        
        return newRelations.stream()
                .map(relation -> {
                    Trainer trainer = trainerDAO.findById(relation.getTrainerId())
                            .orElseThrow(() -> new RuntimeException("Trainer not found"));
                    User user = userService.getUserById(trainer.getUserId());
                    TrainingType specialization = trainingTypeDAO.findById(trainer.getTrainingTypeId())
                            .orElseThrow(() -> new RuntimeException("Training type not found"));
                    
                    return TrainerInfoDto.builder()
                            .username(user.getUsername())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .specialization(specialization)
                            .build();
                })
                .collect(Collectors.toList());
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


	public Long getTraineeIdByUsername(String username) {
	    User user = userService.getByUsername(username);
	    if (user == null) {
	        throw new RuntimeException("Usuario no encontrado: " + username);
	    }
	    Trainee trainee = traineeDAO.findByUserId(user.getId());
	    if (trainee == null) {
	        throw new RuntimeException("Trainee no encontrado para el usuario: " + username);
	    }
	    return trainee.getId();
	} 
	

	
	public void toggleTraineeStatus1(String username, boolean activate) {
	    User user = userService.getByUsername(username);
	    if (user == null) {
	        throw new RuntimeException("Trainee not found with username: " + username);
	    }
	    
	    if (user.getIsActive() == activate) {
	        log.warn("Trainee is already {}", activate ? "active" : "inactive");
	        return;
	    }
	    
	    user.setIsActive(activate);
	    userService.save(user);
	    log.info("Trainee {} successfully {}", username, activate ? "activated" : "deactivated");
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


    
}