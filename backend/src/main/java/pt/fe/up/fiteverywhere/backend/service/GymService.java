package pt.fe.up.fiteverywhere.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.repository.GymRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.GymManagerRepository;

import java.util.Optional;

@Service
public class GymService {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private GymManagerRepository gymManagerRepository;

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
    public void setDailyFeeForGym(Long gymId, Double dailyFee, String email) {
        // Fetch the gym by ID
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new IllegalArgumentException("Gym not found"));
        // Fetch the GymManager by email
        GymManager gymManager = gymManagerRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Gym Manager not found"));
        // Verify that the GymManager is linked to the Gym
        if (!gym.getLinkedGymManagers().contains(gymManager)) {
            throw new SecurityException("Gym Manager does not have access to this gym");
        }
        // Update the dailyFee
        gym.setDailyFee(dailyFee);
        gymRepository.save(gym);
    }

}
