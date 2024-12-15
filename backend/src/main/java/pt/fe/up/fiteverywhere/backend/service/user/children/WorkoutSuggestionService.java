package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.fe.up.fiteverywhere.backend.repository.user.children.WorkoutSuggestionRepository;

@Service
public class WorkoutSuggestionService {

    @Autowired
    private WorkoutSuggestionRepository workoutSuggestionRepository;

    // Delete a workout suggestion by ID
    @Transactional
    public void deleteWorkoutSuggestion(Long id) {
        workoutSuggestionRepository.deleteById(id); // Use the default deleteById method
    }



}
