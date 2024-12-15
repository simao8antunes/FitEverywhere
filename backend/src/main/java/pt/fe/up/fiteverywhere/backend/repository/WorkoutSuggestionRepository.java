package pt.fe.up.fiteverywhere.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.entity.WorkoutSuggestion;

@Repository
public interface WorkoutSuggestionRepository extends JpaRepository<WorkoutSuggestion, Long> {
    List<WorkoutSuggestion> findByClient(Client client);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM workout_suggestion WHERE id = :id", nativeQuery = true)
    void deleteWorkoutSuggestionById(@Param("id") Long id);
}
