package pt.fe.up.fiteverywhere.backend.entity.user.children;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    @ManyToMany(mappedBy = "linkedGymManagers", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Gym> linkedGyms = new HashSet<>();

    public GymManager(String username, String email) {
        super(username, email);
        setRole("gym_manager");
    }

    public GymManager() {
        super();
        setRole("gym_manager");
    }
}
