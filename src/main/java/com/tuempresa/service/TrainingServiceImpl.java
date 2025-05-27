package com.tuempresa.service;

import com.tuempresa.dao.TrainingDAO;
import com.tuempresa.dto.AddTrainingRequestDTO;
import com.tuempresa.dto.TrainerTrainingResponseDTO;
import com.tuempresa.dto.TrainingResponseDTO;
import com.tuempresa.entity.Training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {

	private final TrainingDAO trainingDAO;
	private final TraineeService traineeService;
	private final TrainerService trainerService;

	@Autowired
	public TrainingServiceImpl(TrainingDAO trainingDAO, TraineeService traineeService, TrainerService trainerService) {
		this.trainingDAO = trainingDAO;
		this.traineeService = traineeService;
		this.trainerService = trainerService;
	}

	@Override
	public List<Training> getTrainingsByCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName,
			String trainingType) {
		return trainingDAO.findTrainingsByCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
	}

	@Override
	public List<Training> getTrainingsByTrainerCriteria(String trainerUsername, Date fromDate, Date toDate,
			String traineeName) {
		return trainingDAO.findTrainingsByTrainerCriteria(trainerUsername, fromDate, toDate, traineeName);
	}

	@Override
	public void addTraining(Training training) {
		trainingDAO.addTraining(training);
	}

	

	public List<TrainingResponseDTO> getTraineeTrainings(String username, Date periodFrom, Date periodTo,
			String trainerName, String trainingType) {
		List<Training> trainings = getTrainingsByCriteria(username, periodFrom, periodTo, trainerName, trainingType);

		return trainings.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private TrainingResponseDTO convertToDTO(Training training) {
		TrainingResponseDTO dto = new TrainingResponseDTO();
		dto.setTrainingName(training.getTrainingName());
		dto.setTrainingDate(training.getTrainingDate());
		dto.setTrainingType(training.getTrainingTypeId());
		dto.setTrainingDuration(training.getTrainingDuration());
		dto.setTrainerName(training.getTrainerId());
		return dto;
	}

	

	public List<TrainerTrainingResponseDTO> getTrainerTrainings(String username, Date periodFrom, Date periodTo,
			String traineeName) {
		List<Training> trainings = getTrainingsByTrainerCriteria(username, periodFrom, periodTo, traineeName);

		return trainings.stream().map(this::convertToTrainerDTO).collect(Collectors.toList());
	}

	private TrainerTrainingResponseDTO convertToTrainerDTO(Training training) {
		TrainerTrainingResponseDTO dto = new TrainerTrainingResponseDTO();
		dto.setTrainingName(training.getTrainingName());
		dto.setTrainingDate(training.getTrainingDate());
		dto.setTrainingType(training.getTrainingTypeId());
		dto.setTrainingDuration(training.getTrainingDuration());

		dto.setTraineeName(training.getTrainingName());
		return dto;
	}

	

	@Override
	 public void addTraining(AddTrainingRequestDTO trainingRequest) {
        Training training = new Training();
        training.setTrainingName(trainingRequest.getTrainingName());
        training.setTrainingDate(trainingRequest.getTrainingDate());
        training.setTrainingDuration(trainingRequest.getTrainingDuration());
        
        Long traineeId = traineeService.getTraineeIdByUsername(trainingRequest.getTraineeUsername());
        Long trainerId = trainerService.getTrainerIdByUsername(trainingRequest.getTrainerUsername());
        
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setTrainingTypeId(1L); 
        
        trainingDAO.addTraining(training);
    }
}