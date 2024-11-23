package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.repository.user.children.GymManagerRepository;
import pt.fe.up.fiteverywhere.backend.service.UserService;

import java.util.Optional;

@Service
public class GymManagerService {

    @Autowired
    private GymManagerRepository gymManagerRepository;

    public void save(GymManager gymManager) {
        gymManagerRepository.save(gymManager);
    }

    public Optional<GymManager> findGymManagerByEmail(String email) {
        return gymManagerRepository.findById(email); // Return user if found, otherwise null
    }
}
