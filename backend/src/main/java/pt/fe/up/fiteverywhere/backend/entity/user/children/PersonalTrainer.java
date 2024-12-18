package pt.fe.up.fiteverywhere.backend.entity.user.children;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.User;

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
    @JsonIgnoreProperties("personalTrainer")
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