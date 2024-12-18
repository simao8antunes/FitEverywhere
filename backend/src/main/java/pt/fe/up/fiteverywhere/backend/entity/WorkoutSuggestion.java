package pt.fe.up.fiteverywhere.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;

@Getter
@Setter
@Entity
public class WorkoutSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties("workoutSuggestions")
    private Client client;

    private String time; // e.g., "08:00 to 09:00"
    private String gym;  // Gym name
}