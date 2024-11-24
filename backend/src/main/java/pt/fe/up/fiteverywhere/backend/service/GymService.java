package pt.fe.up.fiteverywhere.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.repository.GymRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.GymManagerRepository;

@Service
public class GymService {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private GymManagerRepository gymManagerRepository;

    public Optional<Gym> getGymById(Long id) {
        return gymRepository.findById(id);
    }

    public Gym saveOrUpdateGym(Gym gym) {
        return gymRepository.save(gym);
    }
    
    public List<Gym> getAllGyms() {
        return gymRepository.findAll();
    }

    @Transactional
    public Gym createGymAndLinkToManager(GymManager gymManager, Gym gym) {

        Gym savedGym = gymRepository.save(gym);

        gymManager.getLinkedGyms().add(savedGym);
        savedGym.getLinkedGymManagers().add(gymManager);

        gymManagerRepository.save(gymManager);

        return savedGym;
    }

}
