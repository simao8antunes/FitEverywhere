package pt.fe.up.fiteverywhere.backend.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;

@Setter
@Getter
@Entity
public class Gym {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private Double dailyFee;

    @Column
    private Double weeklyMembership;

    @ManyToMany(mappedBy = "linkedGyms", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("linkedGyms")
    private Set<GymManager> linkedGymManagers = new HashSet<>();

    @OneToMany(mappedBy = "linkedGym", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("linkedGym")
    private Set<PersonalTrainer> linkedPersonalTrainers = new HashSet<>();

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("gym")
    private Set<Purchase> purchases = new HashSet<>();

    @ManyToMany(mappedBy="favourites", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("favourites")
    private Set<Client> clientFavourites = new HashSet<>();

    public Gym() {
    }

    public Gym(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
