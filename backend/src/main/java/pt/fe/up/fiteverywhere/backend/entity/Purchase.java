package pt.fe.up.fiteverywhere.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_email", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @Column(nullable = false)
    private String type; // "dailyFee" or "weeklyMembership"

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Column(nullable = false)
    private Double price;

    public Purchase() {
    }

    public Purchase(Client client, Gym gym, String type, Double price) {
        this.client = client;
        this.gym = gym;
        this.type = type;
        this.price = price;
        this.purchaseDate = LocalDateTime.now();
    }
}