package com.tuempresa.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuempresa.dao.TraineeDAO;
import com.tuempresa.dao.TraineeTrainerDAO;
import com.tuempresa.dao.TrainerDAO;
import com.tuempresa.dao.TrainingTypeDAO;
import com.tuempresa.dto.CreateGymUserResponseDto;
import com.tuempresa.dto.CreateTrainerRequestDto;
import com.tuempresa.dto.TraineeInfoDto;
import com.tuempresa.dto.TrainerProfileResponseDto;
import com.tuempresa.dto.UnassignedTrainerDto;
import com.tuempresa.dto.UpdateTrainerProfileResponseDto;
import com.tuempresa.dto.UpdateTrainerRequestDto;
import com.tuempresa.entity.Trainee;
import com.tuempresa.entity.Trainer;
import com.tuempresa.entity.TrainingType;
import com.tuempresa.entity.User;

@Service
public class TrainerService {
	private final UserService userService;
	private final TrainerDAO trainerDAO;
	private static final Logger log = LoggerFactory.getLogger(TrainerService.class);


	@Autowired
	private TraineeTrainerDAO traineeTrainerDAO;

	@Autowired
	private TraineeDAO traineeDAO;

	@Autowired
	private TrainingTypeDAO trainingTypeDAO;

	@Autowired
	public TrainerService(UserService userService, TrainerDAO trainerDAO) {
		this.userService = userService;
		this.trainerDAO = trainerDAO;
	}

	public Trainer getTrainerByUsername(String username) {
		User user = userService.getByUsername(username);
		if (user != null) {
			return trainerDAO.findByUserId(user.getId()).orElse(null);
		}
		return null;
	}

	public User getTrainerUserByUsernameUser(String username) {
		User user = userService.getByUsername(username);
		if (user == null) {
			throw new RuntimeException("Usuario no encontrado: " + username);
		}
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

		return new CreateGymUserResponseDto(savedUser.getUsername(), savedUser.getPassword());
	}

	

	public TrainerProfileResponseDto getTrainerProfile(String username) {

		User user = userService.getByUsername(username);
		if (user == null) {
			throw new RuntimeException("Usuario no encontrado: " + username);
		}

		Trainer trainer = trainerDAO.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Trainer no encontrado"));

		TrainingType specialization = trainingTypeDAO.findById(trainer.getTrainingTypeId())
				.orElseThrow(() -> new RuntimeException("Especialización no encontrada"));

		return TrainerProfileResponseDto.builder().firstName(user.getFirstName()).lastName(user.getLastName())
				.specialization(specialization).isActive(user.getIsActive())
				.traineesList(getTraineesForTrainer(trainer.getId())).build();
	}
//AQUI PRIVATE
	 List<TraineeInfoDto> getTraineesForTrainer(Long trainerId) {
		return traineeTrainerDAO.findByTrainerId(trainerId).stream().map(relation -> {
			Trainee trainee = traineeDAO.findById(relation.getTraineeId())
					.orElseThrow(() -> new RuntimeException("Trainee no encontrado"));
			User traineeUser = userService.getUserById(trainee.getUserId());

			return TraineeInfoDto.builder().username(traineeUser.getUsername()).firstName(traineeUser.getFirstName())
					.lastName(traineeUser.getLastName()).build();
		}).collect(Collectors.toList());
	}

	public UpdateTrainerProfileResponseDto updateTrainerProfile(UpdateTrainerRequestDto request) {

		User user = userService.getByUsername(request.getUsername());
		if (user == null) {
			throw new RuntimeException("Usuario no encontrado: " + request.getUsername());
		}

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setIsActive(request.isActive());

		userService.save(user);

		Trainer trainer = trainerDAO.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Trainer not found for user: " + request.getUsername()));

		TrainingType specialization = trainingTypeDAO.findById(trainer.getTrainingTypeId())
				.orElseThrow(() -> new RuntimeException("Training type not found"));

		List<TraineeInfoDto> traineesList = getTraineesForTrainer(trainer.getId());

		return UpdateTrainerProfileResponseDto.builder().username(user.getUsername()).firstName(user.getFirstName())
				.lastName(user.getLastName()).specialization(specialization).isActive(user.getIsActive())
				.traineesList(traineesList).build();

	}

	public List<UnassignedTrainerDto> getUnassignedActiveTrainers(String traineeUsername) {
		User traineeUser = userService.getByUsername(traineeUsername);
		if (traineeUser == null) {
			throw new RuntimeException("Trainee not found: " + traineeUsername);

		}

		List<Trainer> activeTrainers = trainerDAO.findAllActiveTrainers();

		List<Long> assignedTrainerIds = traineeTrainerDAO.findTrainerIdsByTraineeUsername(traineeUsername);

		return activeTrainers.stream().filter(trainer -> !assignedTrainerIds.contains(trainer.getId())).map(trainer -> {
			User trainerUser = userService.getUserById(trainer.getUserId());

			TrainingType specialization = trainingTypeDAO.findById(trainer.getTrainingTypeId())
					.orElseThrow(() -> new RuntimeException("Training type not found"));

			return UnassignedTrainerDto.builder().username(trainerUser.getUsername())
					.firstName(trainerUser.getFirstName()).lastName(trainerUser.getLastName())
					.specialization(specialization).build();
		}).collect(Collectors.toList());

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
	
	
	
	public Long getTrainerIdByUsername(String username) {
	    User user = userService.getByUsername(username);
	    if (user == null) {
	        throw new RuntimeException("Usuario no encontrado: " + username);
	    }
	    Trainer trainer = trainerDAO.findByUserId(user.getId())
	            .orElseThrow(() -> new RuntimeException("Trainer no encontrado para el usuario: " + username));
	    return trainer.getId();
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
		return trainerDAO.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Trainer no encontrado para userId: " + userId));
	}
	
	
	public void toggleTrainerStatus1(String username, boolean activate) {
	    User user = userService.getByUsername(username);
	    if (user == null) {
	        throw new RuntimeException("Trainer not found with username: " + username);
	    }
	    
	    if (user.getIsActive() == activate) {
	        log.warn("Trainer is already {}", activate ? "active" : "inactive");
	        return;
	    }
	    
	    user.setIsActive(activate);
	    userService.save(user);
	    log.info("Trainer {} successfully {}", username, activate ? "activated" : "deactivated");
	}

}