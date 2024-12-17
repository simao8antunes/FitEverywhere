package pt.fe.up.fiteverywhere.backend.entity.user.children;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
    private List<WorkoutSuggestion> workoutSuggestions = new ArrayList<>();


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
