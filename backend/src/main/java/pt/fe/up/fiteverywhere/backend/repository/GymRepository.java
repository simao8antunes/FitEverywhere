package pt.fe.up.fiteverywhere.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.fe.up.fiteverywhere.backend.entity.Gym;

public interface GymRepository extends JpaRepository<Gym, Long> {
}
