package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.repository.PTServiceRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.PersonalTrainerRepository;

import java.util.Optional;

@Service
public class PersonalTrainerService {

    @Autowired
    private PersonalTrainerRepository personalTrainerRepository;

    @Autowired
    private PTServiceRepository ptServiceRepository;

    public void save(PersonalTrainer personalTrainer) {
        personalTrainerRepository.save(personalTrainer);
    }

    public Optional<PersonalTrainer> findPersonalByEmail(String email) {
        return personalTrainerRepository.findById(email); // Return user if found, otherwise null
    }

    public PTService addPTService(String trainerEmail, PTService newService) {
        Optional<PersonalTrainer> trainerOptional = personalTrainerRepository.findById(trainerEmail);
        if (trainerOptional.isEmpty()) {
            throw new IllegalArgumentException("Personal trainer not found");
        }

        PersonalTrainer trainer = trainerOptional.get();

        // Set the relationship
        newService.setPersonalTrainer(trainer);

        // Save the service
        return ptServiceRepository.save(newService);
    }

    public Iterable<PersonalTrainer> getAllPersonalTrainers() {
        return personalTrainerRepository.findAll();
    }



}
