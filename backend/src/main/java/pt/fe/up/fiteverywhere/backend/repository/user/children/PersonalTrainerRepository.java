package pt.fe.up.fiteverywhere.backend.repository.user.children;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;

import java.util.Optional;

public interface PersonalTrainerRepository extends JpaRepository<PersonalTrainer, String> {
    PersonalTrainer findByUsername(String username);

}
