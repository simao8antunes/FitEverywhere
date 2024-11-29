package pt.fe.up.fiteverywhere.backend.entity.user.children;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.User;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
public class GymManager extends User {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "gym_manager_gym",
            joinColumns = @JoinColumn(name = "gym_manager_email", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "gym_id", referencedColumnName = "id")
    )
    @JsonIgnoreProperties("linkedGymManagers")
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
