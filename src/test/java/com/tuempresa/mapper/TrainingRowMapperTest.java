package com.tuempresa.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuempresa.entity.Training;

@ExtendWith(MockitoExtension.class)
class TrainingRowMapperTest {

    private TrainingRowMapper trainingRowMapper;
    
    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        trainingRowMapper = new TrainingRowMapper();
    }

    @Test
    void mapRow_ShouldMapAllFieldsCorrectly() throws SQLException {
        
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("training_name")).thenReturn("Morning Session");
        when(resultSet.getDate("training_date"))
            .thenReturn(Date.valueOf("2023-01-01"));
        when(resultSet.getInt("training_duration")).thenReturn(60);

        
        Training training = trainingRowMapper.mapRow(resultSet, 1);

        
        assertNotNull(training);
        assertEquals(1L, training.getId());
        assertEquals("Morning Session", training.getTrainingName());
        assertEquals("2023-01-01", training.getTrainingDate());
        assertEquals(60, training.getTrainingDuration());
        
        
        verify(resultSet).getLong("id");
        verify(resultSet).getString("training_name");
        verify(resultSet).getDate("training_date");
        verify(resultSet).getInt("training_duration");
        verifyNoMoreInteractions(resultSet);
    }

    @Test
    void mapRow_ShouldHandleNullDate() throws SQLException {
        
        when(resultSet.getLong("id")).thenReturn(2L);
        when(resultSet.getString("training_name")).thenReturn("Evening Session");
        when(resultSet.getDate("training_date")).thenReturn(null);
        when(resultSet.getInt("training_duration")).thenReturn(45);

        
        Training training = trainingRowMapper.mapRow(resultSet, 1);

        
        assertNotNull(training);
        assertEquals(2L, training.getId());
        assertEquals("Evening Session", training.getTrainingName());
        assertNull(training.getTrainingDate());
        assertEquals(45, training.getTrainingDuration());
    }

    @Test
    void mapRow_ShouldThrowExceptionWhenSQLExceptionOccurs() throws SQLException {
        
        when(resultSet.getLong("id")).thenThrow(new SQLException("Database error"));

        
        assertThrows(SQLException.class, () -> {
            trainingRowMapper.mapRow(resultSet, 1);
        });
    }

    @Test
    void mapRow_ShouldUseCorrectRowNumParameter() throws SQLException {
        
        when(resultSet.getLong("id")).thenReturn(3L);
        when(resultSet.getString("training_name")).thenReturn("Test Session");
        when(resultSet.getDate("training_date"))
            .thenReturn(Date.valueOf("2023-02-01"));
        when(resultSet.getInt("training_duration")).thenReturn(30);

        Training training = trainingRowMapper.mapRow(resultSet, 5);

        
        assertNotNull(training);
        assertEquals(3L, training.getId());
        
    }
}