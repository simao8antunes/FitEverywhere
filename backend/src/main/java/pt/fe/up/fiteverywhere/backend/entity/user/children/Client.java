package pt.fe.up.fiteverywhere.backend.entity.user.children;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.User;

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


    public Client(String username, String email) {
        super(username, email);
        setRole("client");
    }

    public Client() {
        super();
        setRole("client");
    }
}
