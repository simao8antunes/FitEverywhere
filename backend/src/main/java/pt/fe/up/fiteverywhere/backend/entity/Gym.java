package pt.fe.up.fiteverywhere.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Gym {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Double dailyFee;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<GymManager> linkedGymManagers = new HashSet<>();

    @OneToMany(mappedBy = "linkedGym", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PersonalTrainer> linkedPersonalTrainers = new HashSet<>();

    public Gym() {
    }

    public Gym(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
