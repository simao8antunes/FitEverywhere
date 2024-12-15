package pt.fe.up.fiteverywhere.backend.entity.user.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.Purchase;
import pt.fe.up.fiteverywhere.backend.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Client extends User {

    @Column
    private Integer workoutsPerWeek;

    @Column
    private String preferredTime;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("client")
    private Set<Purchase> purchases = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "client_ptservice",
            joinColumns = @JoinColumn(name = "client_email", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "ptservice_id", referencedColumnName = "id")
    )
    @JsonIgnoreProperties("clients")
    private Set<PTService> ptServices = new HashSet<>();

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
