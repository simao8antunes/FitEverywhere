package pt.fe.up.fiteverywhere.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class PTService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double price;

    @Column
    private Integer duration;

    @Column
    private String type;

    @ManyToOne
    @JoinColumn(name = "personal_trainer_email")
    @JsonIgnoreProperties("services")
    private PersonalTrainer personalTrainer;

    @ManyToMany(mappedBy = "ptServices", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("ptServices")
    private Set<Client> clients = new HashSet<>();

    public PTService() {
    }
}