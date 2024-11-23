package pt.fe.up.fiteverywhere.backend.entity.user.children;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.User;

@Getter
@Setter
@Entity
public class PersonalTrainer extends User {

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym linkedGym;

    public PersonalTrainer(String username, String email) {
        super(username, email);
        setRole("personal_trainer");
    }

    public PersonalTrainer() {
        super();
        setRole("personal_trainer");
    }
}
