package pt.fe.up.fiteverywhere.backend.entity.user.children;

import java.util.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.entity.WorkoutSuggestion;

@Getter
@Setter
@Entity
public class Client extends User {

    @Column()
    private Integer workoutsPerWeek;

    @Column()
    private String preferredTime;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<WorkoutSuggestion> workoutSuggestions = new HashSet<>();

    public Client(String username, String email) {
        super(username, email);
        setRole("client");
    }

    public Client() {
        super();
        setRole("client");
    }
}
