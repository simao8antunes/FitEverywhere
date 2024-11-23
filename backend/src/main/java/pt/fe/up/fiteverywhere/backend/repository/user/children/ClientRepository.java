package pt.fe.up.fiteverywhere.backend.repository.user.children;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {
    Client findByUsername(String username);

}
