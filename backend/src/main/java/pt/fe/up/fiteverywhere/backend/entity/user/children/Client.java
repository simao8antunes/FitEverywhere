package pt.fe.up.fiteverywhere.backend.entity.user.children;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pt.fe.up.fiteverywhere.backend.entity.User;

@Getter
@Setter
@Entity
public class Client extends User {

    @Column()
    private Integer workoutsPerWeek;

    @Column()
    private String preferredTime;

    public Client(String username, String email) {
        super(username, email);
    }

    public Client() {
        super();
    }
}
