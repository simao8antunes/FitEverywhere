package pt.fe.up.fiteverywhere.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.service.GymService;
import pt.fe.up.fiteverywhere.backend.service.user.children.GymManagerService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/gym")
public class GymController {

    @Autowired
    private GymService gymService;

    @Autowired
    private GymManagerService gymManagerService;

    // CREATE A GYM
    @PostMapping("/")
    public ResponseEntity<String> createGym(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody Gym gym
    ) {
        String email = principal.getAttribute("email");
        System.out.println("Creating gym of user: " + email); // Debug log
        Optional<GymManager> gymManagerOpt = gymManagerService.findGymManagerByEmail(email);
        if (gymManagerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        GymManager gymManager = gymManagerOpt.get();
        Gym createdGym = gymService.createGymAndLinkToManager(gymManager, gym);
        return ResponseEntity.ok("Successfully created gym with id: " + createdGym.getId());
    }

    // UPDATE A GYM
    @Transactional
    @PutMapping("/")
    public ResponseEntity<?> updateGymDetails(@RequestBody Gym gym, @AuthenticationPrincipal OAuth2User principal) {
        Optional<GymManager> existingUserOpt = gymManagerService.findGymManagerByEmail(principal.getAttribute("email"));
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found for the provided email."));
        }


        GymManager gymManager = existingUserOpt.get();
        // Check if the gym is associated with the current GymManager
        Optional<Gym> gymOpt = gymService.getGymById(gym.getId());

        if (gymOpt.isEmpty() || !gymOpt.get().getLinkedGymManagers().contains(gymManager)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Gym not found or not authorized to update."));
        }

        // Update gym details
        Gym existingGym = gymOpt.get();
        existingGym.setName(gym.getName());
        existingGym.setDailyFee(gym.getDailyFee());
        existingGym.setLatitude(gym.getLatitude());
        existingGym.setLongitude(gym.getLongitude());

        gymService.saveOrUpdateGym(existingGym);

        return ResponseEntity.ok(Map.of("message", "Gym details updated successfully!"));
    }

    // GET GYM DETAILS
    @GetMapping("/")
    public ResponseEntity<?> getGymDetails(@AuthenticationPrincipal OAuth2User principal) {

        Optional<GymManager> existingUserOpt = gymManagerService.findGymManagerByEmail(principal.getAttribute("email"));
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found for the provided email."));
        }

        GymManager gymManager = existingUserOpt.get();
        // Retrieve the first associated gym for simplicity (adjust logic if there are multiple gyms)
        Optional<Gym> gymOpt = gymManager.getLinkedGyms().stream().findFirst();

        if (gymOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No gym found for the current user."));
        }

        Gym gym = gymOpt.get();
        Map<String, Object> gymInfo = Map.of(
                "name", gym.getName(),
                "dailyFee", gym.getDailyFee(),
                "latitude", gym.getLatitude(),
                "longitude", gym.getLongitude()
        );

        return ResponseEntity.ok(gymInfo);
    }


}
