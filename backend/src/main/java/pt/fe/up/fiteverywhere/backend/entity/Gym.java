package pt.fe.up.fiteverywhere.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@PrimaryKeyJoinColumn(name = "user_id") // Link Gym with User via inheritance
public class Gym extends User {

    @Column(nullable = false)
    private String gymName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private String facilities; // e.g., "Pool, Sauna, Gym Equipment"

    @Column(nullable = false)
    private Double dailyFee;

    public Gym() {
        super();
        this.setRole("gym"); // Default role for a Gym user
    }

    public Gym(String username, String email, String password, String gymName, String location) {
        super(username, email, password);
        this.gymName = gymName;
        this.location = location;
        this.setRole("gym"); // Assign role as "gym"
    }
}
