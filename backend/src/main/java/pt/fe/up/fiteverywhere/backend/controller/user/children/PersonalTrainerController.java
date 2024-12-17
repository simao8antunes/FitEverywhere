package pt.fe.up.fiteverywhere.backend.controller.user.children;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.service.UserService;
import pt.fe.up.fiteverywhere.backend.service.user.children.PersonalTrainerService;

@RestController
@RequestMapping("/personal-trainer")
public class PersonalTrainerController {

    @Autowired
    private PersonalTrainerService personalTrainerService;
    @Autowired
    private UserService userService;

    @PostMapping("/add-service")
    public ResponseEntity<String> addService(@RequestBody PTService serviceDTO,
            @AuthenticationPrincipal OAuth2User principal) {
        // Get the trainer's email from the logged-in user
        String trainerEmail = principal.getAttribute("email");
        Optional<PersonalTrainer> gymManagerOpt = personalTrainerService.findPersonalByEmail(trainerEmail);
        if (gymManagerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        try {
            // Map DTO to PTService entity
            PTService newService = new PTService();
            newService.setId(serviceDTO.getId() == 0 ? null : serviceDTO.getId());
            newService.setName(serviceDTO.getName());
            newService.setDescription(serviceDTO.getDescription());
            newService.setPrice(serviceDTO.getPrice());
            newService.setDuration(serviceDTO.getDuration());
            newService.setType(serviceDTO.getType());

            PTService createdService = personalTrainerService.addPTService(trainerEmail, newService);
            return ResponseEntity.ok("Service added successfully with id: " + createdService.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getPersonalTrainers(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Iterable<PersonalTrainer> personalTrainers = personalTrainerService.getAllPersonalTrainers();
        return ResponseEntity.ok(personalTrainers);
    }

    @GetMapping("/get-available")
    public ResponseEntity<?> getAvailablePTs() {
        Iterable<PersonalTrainer> availablePTs = personalTrainerService.getAvailablePTs();
        if (availablePTs == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("error", "No available pts found."));
        }
        return ResponseEntity.ok(availablePTs);
    }

}
