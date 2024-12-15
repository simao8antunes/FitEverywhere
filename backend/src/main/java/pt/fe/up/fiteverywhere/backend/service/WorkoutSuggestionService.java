package pt.fe.up.fiteverywhere.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.WorkoutSuggestion;
import pt.fe.up.fiteverywhere.backend.repository.WorkoutSuggestionRepository;

import java.util.Optional;

@Service
public class WorkoutSuggestionService {

    @Autowired
    private WorkoutSuggestionRepository workoutSuggestionRepository;

    public Optional<WorkoutSuggestion> getWorkoutSuggestionById(Long id) {
        return workoutSuggestionRepository.findById(id);
    }

    @Transactional
    public void deleteWorkoutSuggestion(Long id) {
        workoutSuggestionRepository.deleteWorkoutSuggestionById(id);
    }

}
