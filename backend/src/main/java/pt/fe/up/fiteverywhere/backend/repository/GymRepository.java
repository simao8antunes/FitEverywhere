package pt.fe.up.fiteverywhere.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.fe.up.fiteverywhere.backend.entity.Gym;

public interface GymRepository extends JpaRepository<Gym, Long> {

    @Query("SELECT g FROM Gym g WHERE g.gymName = :name AND " +
           "ABS(g.latitude - :latitude) < 0.001 AND " +
           "ABS(g.longitude - :longitude) < 0.001")
    Optional<Gym> findGymByNameAndLocation(@Param("name") String name,
                                           @Param("latitude") double latitude,
                                           @Param("longitude") double longitude);


}
