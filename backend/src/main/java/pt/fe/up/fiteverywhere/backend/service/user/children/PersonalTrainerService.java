package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.repository.user.children.PersonalTrainerRepository;

import java.util.Optional;

@Service
public class PersonalTrainerService {

    @Autowired
    private PersonalTrainerRepository personalTrainerRepository;

    public void save(PersonalTrainer personalTrainer) {
        personalTrainerRepository.save(personalTrainer);
    }

    public Optional<PersonalTrainer> findPersonalByEmail(String email) {
        return personalTrainerRepository.findById(email); // Return user if found, otherwise null
    }
}
