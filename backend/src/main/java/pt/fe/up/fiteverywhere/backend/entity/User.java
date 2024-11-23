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
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Setter
@Getter
@Entity
@EntityScan("pt.fe.up.fiteverywhere.backend.entity")
// Define inheritance strategy
public abstract class User {

    @Id
    @Column(nullable = false, unique = true)  // Ensure email is unique and not null
    private String email;

    @Column(nullable = false)  // En§sure username is unique and not null
    private String username;

    @Column
    private String role;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}