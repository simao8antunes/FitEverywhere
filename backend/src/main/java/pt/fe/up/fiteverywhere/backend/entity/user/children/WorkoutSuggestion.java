package pt.fe.up.fiteverywhere.backend.entity.user.children;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class WorkoutSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Client client;

    private String time; // e.g., "08:00 to 09:00"
    private String gym;  // Gym name
}