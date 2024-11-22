package pt.fe.up.fiteverywhere.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.service.GymService;

import java.util.Optional;

@RestController
@RequestMapping("/gym")
public class GymController {

    @Autowired
    private GymService gymService;

    @GetMapping("/details")
    public ResponseEntity<?> getGymDetails(@RequestParam Long id) {

        Optional<Gym> gym = gymService.getGymById(id);
        if (gym.isPresent()) {
            return ResponseEntity.ok(gym.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gym not found");
        }


    }

    @PostMapping("/details")
    public ResponseEntity<?> saveGymDetails(@RequestBody Gym gym) {
        Gym savedGym = gymService.saveOrUpdateGym(gym);
        return ResponseEntity.ok(savedGym);
    }
}
