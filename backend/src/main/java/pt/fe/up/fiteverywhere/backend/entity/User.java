package pt.fe.up.fiteverywhere.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Define inheritance strategy
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Automatically generate IDs
    private Long id;

    @Column(nullable = false, unique = false)  // Ensure username is unique and not null
    private String username;

    @Column(nullable = false, unique = true)  // Ensure email is unique and not null
    private String email;

    @Column(nullable = false)  // Password should not be null
    private String password;

    @Column(nullable = true)  // Role can be null initially
    private String role;
    
    @Column(nullable = true)
    private Integer workoutsPerWeek;
    
    @Column(nullable = true)
    private String preferredTime; // morning, afternoon, evening
    

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}