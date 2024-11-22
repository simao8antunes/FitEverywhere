package pt.fe.up.fiteverywhere.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.repository.GymRepository;

@Service
public class GymService {

    @Autowired
    private GymRepository gymRepository;

    public Optional<Gym> getGymById(Long id) {
        return gymRepository.findById(id);
    }

    public Gym saveOrUpdateGym(Gym gym) {
        return gymRepository.save(gym);
    }

    public Optional<Gym> findGymByNameAndLocation(String name, double latitude, double longitude) {
        return gymRepository.findGymByNameAndLocation(name, latitude, longitude);
    }
    
}
