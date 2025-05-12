package com.tuempresa.mapper;

import com.tuempresa.entity.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingRowMapperTest {

    private ResultSet resultSet;
    private TrainingRowMapper trainingRowMapper;

    @BeforeEach
    void setUp() {
        resultSet = mock(ResultSet.class);
        trainingRowMapper = new TrainingRowMapper();
    }

    @Test
    void testMapRowWithValidData() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("training_name")).thenReturn("Java Basics");
        when(resultSet.getDate("training_date")).thenReturn(Date.valueOf("2025-04-21"));
        when(resultSet.getInt("training_duration")).thenReturn(90);

        Training training = trainingRowMapper.mapRow(resultSet, 1);

        assertNotNull(training);
        assertEquals(1L, training.getId());
        assertEquals("Java Basics", training.getTrainingName());
        assertEquals("2025-04-21", training.getTrainingDate());
        assertEquals(90, training.getTrainingDuration());
    }

    @Test
    void testMapRowWithNullDate() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(2L);
        when(resultSet.getString("training_name")).thenReturn("Spring Boot");
        when(resultSet.getDate("training_date")).thenReturn(null); 
        when(resultSet.getInt("training_duration")).thenReturn(120);

        Training training = trainingRowMapper.mapRow(resultSet, 2);

        assertNotNull(training);
        assertEquals(2L, training.getId());
        assertEquals("Spring Boot", training.getTrainingName());
        assertNull(training.getTrainingDate());
        assertEquals(120, training.getTrainingDuration());
    }
}
