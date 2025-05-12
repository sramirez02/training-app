package com.tuempresa.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tuempresa.entity.Training;

public class TrainingRowMapper implements RowMapper<Training> {

    @Override
    public Training mapRow(ResultSet rs, int rowNum) throws SQLException {
        Training training = new Training();

        training.setId(rs.getLong("id"));
        training.setTrainingName(rs.getString("training_name"));
        Date date = rs.getDate("training_date");
        training.setTrainingDate(date != null ? date.toString() : null);
        training.setTrainingDuration(rs.getInt("training_duration"));

        return training;
    }
}