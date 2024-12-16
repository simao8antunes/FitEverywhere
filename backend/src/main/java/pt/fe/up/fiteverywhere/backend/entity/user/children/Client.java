package pt.fe.up.fiteverywhere.backend.entity.user.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.Purchase;
import pt.fe.up.fiteverywhere.backend.entity.User;

import java.util.HashSet;
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

    public Client(String username, String email) {
        super(username, email);
        setRole("client");
    }

    public Client() {
        super();
        setRole("client");
    }
}
