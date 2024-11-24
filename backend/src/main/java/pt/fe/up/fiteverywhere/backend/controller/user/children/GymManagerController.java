package pt.fe.up.fiteverywhere.backend.controller.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.service.GymService;
import pt.fe.up.fiteverywhere.backend.service.user.children.GymManagerService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gym-manager")
public class GymManagerController {

    @Autowired
    private GymService gymService;
    @Autowired
    private GymManagerService gymManagerService;

    @PostMapping("/create-gym")
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

    @GetMapping("/list-gyms")
    public ResponseEntity<?> listGyms(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        System.out.println("Getting gyms of user: " + email); // Debug log
        Optional<GymManager> gymManagerOpt = gymManagerService.findGymManagerByEmail(email);
        if (gymManagerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        GymManager gymManager = gymManagerOpt.get();
        Set<Gym> linkedGyms = gymManager.getLinkedGyms();

        if (linkedGyms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No gyms found");
        }

        List<Map<String, Object>> gymList = linkedGyms.stream()
                .map(gym -> {
                    Map<String, Object> gymData = new HashMap<>();
                    gymData.put("id", gym.getId());
                    gymData.put("name", gym.getName());
                    gymData.put("dailyFee", gym.getDailyFee());
                    gymData.put("latitude", gym.getLatitude());
                    gymData.put("longitude", gym.getLongitude());
                    return gymData;
                })
                .toList();

        // Convert gyms to a simpler DTO (Data Transfer Object) if necessary
        return ResponseEntity.ok(gymList);
    }
}
