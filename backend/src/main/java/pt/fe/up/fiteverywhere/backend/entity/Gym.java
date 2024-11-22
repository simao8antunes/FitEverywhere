package pt.fe.up.fiteverywhere.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Gym extends User {

    @Column(nullable = false)
    private String gymName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private String facilities; // e.g., "Pool, Sauna, Gym Equipment"

    @Column(nullable = false)
    private Double dailyFee;

    @Column(nullable = false)
    private Double latitude; // Latitude of the gym

    @Column(nullable = false)
    private Double longitude; // Longitude of the gym

    public Gym() {
        super();
        this.setRole("gym"); // Default role for a Gym user
    }

    public Gym(String username, String email, String password, String gymName, String location, Double latitude, Double longitude) {
        super(username, email, password);
        this.gymName = gymName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.setRole("gym"); // Assign role as "gym"
    }
}
