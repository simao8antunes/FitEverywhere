package pt.fe.up.fiteverywhere.backend.entity.user.children;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class PersonalTrainer extends User {

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym linkedGym;

    @OneToMany(mappedBy = "personalTrainer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PTService> services = new HashSet<>();

    public PersonalTrainer(String username, String email) {
        super(username, email);
        setRole("personal_trainer");
    }

    public PersonalTrainer() {
        super();
        setRole("personal_trainer");
    }
}