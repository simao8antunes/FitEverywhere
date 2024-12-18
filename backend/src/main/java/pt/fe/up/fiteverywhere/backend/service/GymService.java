package pt.fe.up.fiteverywhere.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.repository.GymRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.GymManagerRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.PersonalTrainerRepository;

import java.util.Optional;

@Service
public class GymService {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private GymManagerRepository gymManagerRepository;

    @Autowired
    private PersonalTrainerRepository personalTrainerRepository;

    public Optional<Gym> getGymById(Long id) {
        return gymRepository.findById(id);
    }

    public void saveOrUpdateGym(Gym gym) {
        gymRepository.save(gym);
    }

    @Transactional
    public Gym createGymAndLinkToManager(GymManager gymManager, Gym gym) {

        Gym savedGym = gymRepository.save(gym);

        gymManager.getLinkedGyms().add(savedGym);
        savedGym.getLinkedGymManagers().add(gymManager);

        gymManagerRepository.save(gymManager);

        return savedGym;
    }

    @Transactional
    public Gym linkPersonalTrainer(Gym gym, PersonalTrainer personalTrainer) {

        Gym savedGym = gymRepository.save(gym);

        personalTrainer.setLinkedGym(savedGym);
        savedGym.getLinkedPersonalTrainers().add(personalTrainer);

        personalTrainerRepository.save(personalTrainer);

        return savedGym;
    }

    public Iterable<Gym> getAllGyms() {
        return gymRepository.findAll();
    }

}
