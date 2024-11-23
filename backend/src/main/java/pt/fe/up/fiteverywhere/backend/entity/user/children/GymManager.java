package pt.fe.up.fiteverywhere.backend.entity.user.children;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.User;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
public class GymManager extends User {

    @ManyToMany
    @JoinTable(
            name = "gym_manager_gym", // Name of the join table
            joinColumns = @JoinColumn(name = "gym_manager_id"), // Foreign key for GymManager
            inverseJoinColumns = @JoinColumn(name = "gym_id")  // Foreign key for Gym
    )
    private Set<Gym> linkedGyms = new HashSet<>();

    public GymManager(String username, String email) {
        super(username, email);
    }

    public GymManager() {
        super();
    }
}
