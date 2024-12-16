package pt.fe.up.fiteverywhere.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.fe.up.fiteverywhere.backend.entity.PTService;

@Repository
public interface PTServiceRepository extends JpaRepository<PTService, Long> {
}
