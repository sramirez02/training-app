package com.tuempresa.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuempresa.entity.Training;

@ExtendWith(MockitoExtension.class)
class TrainingRowMapperTest {

    @Mock
    private ResultSet resultSet;

    private TrainingRowMapper trainingRowMapper;

    @BeforeEach
    void setUp() {
        trainingRowMapper = new TrainingRowMapper();
    }

    @Test
    void testMapRow_WithAllFields() throws SQLException {
        // Arrange
        long expectedId = 1L;
        String expectedName = "Yoga Session";
        Date expectedDate = Date.valueOf(LocalDate.of(2023, 6, 15));
        int expectedDuration = 60;

        when(resultSet.getLong("id")).thenReturn(expectedId);
        when(resultSet.getString("training_name")).thenReturn(expectedName);
        when(resultSet.getDate("training_date")).thenReturn(expectedDate);
        when(resultSet.getInt("training_duration")).thenReturn(expectedDuration);

        // Act
        Training training = trainingRowMapper.mapRow(resultSet, 0);

        // Assert
        assertNotNull(training);
        assertEquals(expectedId, training.getId());
        assertEquals(expectedName, training.getTrainingName());
        assertEquals(expectedDate.toString(), training.getTrainingDate());
        assertEquals(expectedDuration, training.getTrainingDuration());
    }

    @Test
    void testMapRow_WithNullDate() throws SQLException {
        // Arrange
        long expectedId = 2L;
        String expectedName = "Cardio Session";
        Date nullDate = null;
        int expectedDuration = 45;

        when(resultSet.getLong("id")).thenReturn(expectedId);
        when(resultSet.getString("training_name")).thenReturn(expectedName);
        when(resultSet.getDate("training_date")).thenReturn(nullDate);
        when(resultSet.getInt("training_duration")).thenReturn(expectedDuration);

        // Act
        Training training = trainingRowMapper.mapRow(resultSet, 0);

        // Assert
        assertNotNull(training);
        assertEquals(expectedId, training.getId());
        assertEquals(expectedName, training.getTrainingName());
        assertNull(training.getTrainingDate());
        assertEquals(expectedDuration, training.getTrainingDuration());
    }

    @Test
    void testMapRow_WithSQLException() throws SQLException {
        // Arrange
        when(resultSet.getLong("id")).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(SQLException.class, () -> {
            trainingRowMapper.mapRow(resultSet, 0);
        });
    }

    @Test
    void testMapRow_WithMinimumFields() throws SQLException {
        // Arrange
        long expectedId = 3L;
        String expectedName = "Basic Training";
        // No date provided
        int expectedDuration = 30;

        when(resultSet.getLong("id")).thenReturn(expectedId);
        when(resultSet.getString("training_name")).thenReturn(expectedName);
        when(resultSet.getDate("training_date")).thenReturn(null);
        when(resultSet.getInt("training_duration")).thenReturn(expectedDuration);

        // Act
        Training training = trainingRowMapper.mapRow(resultSet, 0);

        // Assert
        assertNotNull(training);
        assertEquals(expectedId, training.getId());
        assertEquals(expectedName, training.getTrainingName());
        assertNull(training.getTrainingDate());
        assertEquals(expectedDuration, training.getTrainingDuration());
    }
}