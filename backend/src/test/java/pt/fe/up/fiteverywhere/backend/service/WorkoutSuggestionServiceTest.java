package pt.fe.up.fiteverywhere.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.fe.up.fiteverywhere.backend.entity.WorkoutSuggestion;
import pt.fe.up.fiteverywhere.backend.repository.WorkoutSuggestionRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WorkoutSuggestionServiceTest {

    @Mock
    private WorkoutSuggestionRepository workoutSuggestionRepository;

    @InjectMocks
    private WorkoutSuggestionService workoutSuggestionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWorkoutSuggestionById_ShouldReturnWorkoutSuggestion_WhenExists() {
        // Arrange
        WorkoutSuggestion suggestion = mock(WorkoutSuggestion.class);
        when(workoutSuggestionRepository.findById(1L)).thenReturn(Optional.of(suggestion));

        // Act
        Optional<WorkoutSuggestion> result = workoutSuggestionService.getWorkoutSuggestionById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(suggestion);
    }

    @Test
    void getWorkoutSuggestionById_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(workoutSuggestionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<WorkoutSuggestion> result = workoutSuggestionService.getWorkoutSuggestionById(1L);

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void deleteWorkoutSuggestion_ShouldDeleteById() {
        // Act
        workoutSuggestionService.deleteWorkoutSuggestion(1L);

        // Assert
        verify(workoutSuggestionRepository).deleteWorkoutSuggestionById(1L);
    }
}
