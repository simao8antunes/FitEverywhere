package pt.fe.up.fiteverywhere.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.fe.up.fiteverywhere.backend.entity.Gym;

public interface GymRepository extends JpaRepository<Gym, Long> {

}
