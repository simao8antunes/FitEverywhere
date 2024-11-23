package pt.fe.up.fiteverywhere.backend.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gymName;

    @Column(nullable = false)
    private String location;

    @Column()
    private String facilities; // e.g., "Pool, Sauna, Gym Equipment"

    @Column(nullable = false)
    private Double dailyFee;

    @Column()
    private Double latitude; // Latitude of the gym

    @Column()
    private Double longitude; // Longitude of the gym

    @ManyToMany
    private Set<GymManager> linkedGymManagers= new HashSet<>();

    @OneToMany(mappedBy = "linkedGym", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonalTrainer> linkedPersonalTrainers = new HashSet<>();

    public Gym() {
    }

    public Gym(String gymName, String location, Double latitude, Double longitude) {
        this.gymName = gymName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
