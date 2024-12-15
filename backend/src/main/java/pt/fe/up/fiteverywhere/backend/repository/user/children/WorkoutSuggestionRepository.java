package pt.fe.up.fiteverywhere.backend.repository.user.children;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.entity.user.children.WorkoutSuggestion;

public interface WorkoutSuggestionRepository extends JpaRepository<WorkoutSuggestion, Long> {
    List<WorkoutSuggestion> findByClientEmail(String email);
    List<WorkoutSuggestion> findByClient(Client client);
    Optional<WorkoutSuggestion> findById(Long id);
}
