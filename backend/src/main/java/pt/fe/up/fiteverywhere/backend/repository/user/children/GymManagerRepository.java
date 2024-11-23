package pt.fe.up.fiteverywhere.backend.repository.user.children;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;

import java.util.Optional;

public interface GymManagerRepository extends JpaRepository<GymManager, String> {
    GymManager findByUsername(String username);

}
