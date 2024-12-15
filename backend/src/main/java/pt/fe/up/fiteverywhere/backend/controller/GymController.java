package pt.fe.up.fiteverywhere.backend.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.service.GymService;
import pt.fe.up.fiteverywhere.backend.service.user.children.GymManagerService;

@RestController
@RequestMapping("/gym")
public class GymController {

    @Autowired
    private GymService gymService;

    @Autowired
    private GymManagerService gymManagerService;

    // CREATE A GYM
    @PostMapping("/{id}")
    public ResponseEntity<String> createGym(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam String name,
            @PathVariable Long id
    ) {
        String email = principal.getAttribute("email");
        System.out.println("Creating gym of user: " + email); // Debug log
        Optional<GymManager> gymManagerOpt = gymManagerService.findGymManagerByEmail(email);
        if (gymManagerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        GymManager gymManager = gymManagerOpt.get();
        Gym newGym = new Gym(id, name);
        Gym createdGym = gymService.createGymAndLinkToManager(gymManager, newGym);
        return ResponseEntity.ok("Successfully created gym with id: " + createdGym.getId());
    }

    // UPDATE A GYM
    @Transactional
    @PutMapping("/{id}")
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
        existingGym.setDescription(gym.getDescription());
        existingGym.setDailyFee(gym.getDailyFee());
        existingGym.setWeeklyMembership(gym.getWeeklyMembership());
        
        gymService.saveOrUpdateGym(existingGym);

        return ResponseEntity.ok(Map.of("message", "Gym details updated successfully!"));
    }

    // GET GYM DETAILS
    @GetMapping("/{id}")
    public ResponseEntity<?> getGymDetails(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {

        Optional<GymManager> existingUserOpt = gymManagerService.findGymManagerByEmail(principal.getAttribute("email"));
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found for the provided email."));
        }

        GymManager gymManager = existingUserOpt.get();
        // Retrieve the first associated gym for simplicity (adjust logic if there are multiple gyms)
        Optional<Gym> gymOpt = gymManager.getLinkedGyms().stream().filter(gym -> gym.getId().equals(id)).findFirst();

        if (gymOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No gym found for the current user."));
        }

        Gym gym = gymOpt.get();
        Map<String, Object> gymInfo = new HashMap<>();
        gymInfo.put("id", gym.getId());
        gymInfo.put("name", gym.getName());
        gymInfo.put("description", gym.getDescription());
        gymInfo.put("dailyFee", gym.getDailyFee());
        gymInfo.put("weeklyMembership", gym.getWeeklyMembership());
        gymInfo.put("personalTrainers", gym.getLinkedPersonalTrainers());
        RestTemplate restTemplate = new RestTemplate();
        String overpassQuery = "[out:json];node(" + gym.getId() + ");out body;";
        String overpassUrl = "https://overpass-api.de/api/interpreter?data=" + overpassQuery;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(overpassUrl, Map.class);
            Map overpassData = response.getBody();
            assert overpassData != null && overpassData.get("elements") != null;
            List<Map<String, Object>> elements = (List<Map<String, Object>>) overpassData.get("elements");
            gymInfo.put("overpassData", elements.get(0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Failed to fetch data from Overpass API"));
        }

        return ResponseEntity.ok(gymInfo);
    }


    // GET ALL GYMS
    @GetMapping("/all")
    public ResponseEntity<?> getAllGyms() {
        Iterable<Gym> gyms = gymService.getAllGyms();
        if (gyms == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("error", "No gyms found."));
        }
        return ResponseEntity.ok(gyms);
    }

}
