package com.tuempresa.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.tuempresa.entity.Training;
import com.tuempresa.mapper.TrainingRowMapper;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    private DataSource dataSource;

    public TrainingDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Training> findTrainingsByCriteria(String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        List<Training> trainings = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
        		"SELECT t.*, " +
        			    "trainee_user.username AS trainee_username, " +
        			    "trainer_user.first_name AS trainer_first_name, " +
        			    "trainer_user.last_name AS trainer_last_name, " +
        			    "tt.training_type_name " +
        			    "FROM training t " +
        			    "JOIN trainee tr ON t.trainee_id = tr.id " +
        			    "JOIN app_user trainee_user ON tr.user_id = trainee_user.id " +
        			    "JOIN trainer tn ON t.trainer_id = tn.id " +
        			    "JOIN app_user trainer_user ON tn.user_id = trainer_user.id " +
        			    "JOIN training_type tt ON t.training_type_id = tt.id " +
        			    "WHERE 1=1"
        );

                
        List<Object> params = new ArrayList<>();

        if (username != null && !username.isEmpty()) {
            sql.append(" AND trainee_user.username = ?");
            params.add(username);
        }
        if (fromDate != null) {
            sql.append(" AND t.training_date >= ?");
            params.add(new java.sql.Date(fromDate.getTime()));
        }
        if (toDate != null) {
            sql.append(" AND t.training_date <= ?");
            params.add(new java.sql.Date(toDate.getTime()));
        }
        if (trainerName != null && !trainerName.isEmpty()) {
            sql.append(" AND CONCAT(trainer_user.first_name, ' ', trainer_user.last_name) = ?");
            params.add(trainerName);
        }
        if (trainingType != null && !trainingType.isEmpty()) {
            sql.append(" AND tt.training_type_name = ?");
            params.add(trainingType);
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            TrainingRowMapper rowMapper = new TrainingRowMapper();
            int rowNum = 0;

            while (rs.next()) {
                trainings.add(rowMapper.mapRow(rs, rowNum++));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainings;
    }
    
    
    public List<Training> findTrainingsByTrainerCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        List<Training> trainings = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT t.*, " +
            "trainer_user.username AS trainer_username, " +
            "trainee_user.first_name AS trainee_first_name, " +
            "trainee_user.last_name AS trainee_last_name " +
            "FROM training t " +
            "JOIN trainer trn ON t.trainer_id = trn.id " +
            "JOIN app_user trainer_user ON trn.user_id = trainer_user.id " +
            "JOIN trainee tra ON t.trainee_id = tra.id " +
            "JOIN app_user trainee_user ON tra.user_id = trainee_user.id " +
            "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (trainerUsername != null && !trainerUsername.isEmpty()) {
            sql.append(" AND trainer_user.username = ?");
            params.add(trainerUsername);
        }

        if (fromDate != null) {
            sql.append(" AND t.training_date >= ?");
            params.add(new java.sql.Date(fromDate.getTime()));
        }

        if (toDate != null) {
            sql.append(" AND t.training_date <= ?");
            params.add(new java.sql.Date(toDate.getTime()));
        }

        if (traineeName != null && !traineeName.isEmpty()) {
            sql.append(" AND CONCAT(trainee_user.first_name, ' ', trainee_user.last_name) = ?");
            params.add(traineeName);
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            TrainingRowMapper rowMapper = new TrainingRowMapper();
            int rowNum = 0;

            while (rs.next()) {
                trainings.add(rowMapper.mapRow(rs, rowNum++));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainings;
}
    
    @Override
    public void addTraining(Training training) {
        String sql = "INSERT INTO training (training_name, training_date, training_duration, trainee_id, trainer_id, training_type_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, training.getTrainingName());
            ps.setString(2, training.getTrainingDate());
            ps.setInt(3, training.getTrainingDuration());
            ps.setLong(4, training.getTraineeId()); 
            ps.setLong(5, training.getTrainerId());
            ps.setLong(6, training.getTrainingTypeId());

            ps.executeUpdate();
            System.out.println("Entrenamiento agregado con éxito");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}