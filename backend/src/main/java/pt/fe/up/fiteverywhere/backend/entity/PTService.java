package pt.fe.up.fiteverywhere.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;

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

    public PTService(Long id, String name, String description, Double price, Integer duration, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.type = type;
    }

    public PTService() {

    }
}