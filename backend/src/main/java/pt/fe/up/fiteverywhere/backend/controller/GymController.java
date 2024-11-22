package pt.fe.up.fiteverywhere.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.service.GymService;
import pt.fe.up.fiteverywhere.backend.service.UserService;

@RestController
@RequestMapping("/gym")
public class GymController {

    @Autowired
    private GymService gymService;

    @Autowired
    private UserService userService;

    @PostMapping("/details")
    public ResponseEntity<?> saveGymDetails(@RequestBody Gym gym) {
        User existingUser = userService.findUserByEmail(gym.getEmail());
        if (existingUser == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found for the provided email."));
        }

        if (existingUser instanceof Gym) {
            Gym existingGym = (Gym) existingUser;
            existingGym.setGymName(gym.getGymName());
            existingGym.setLocation(gym.getLocation());
            existingGym.setFacilities(gym.getFacilities());
            existingGym.setDailyFee(gym.getDailyFee());
            existingGym.setLatitude(gym.getLatitude());
            existingGym.setLongitude(gym.getLongitude());
        } else {
            Gym newGym = new Gym(existingUser.getUsername(), existingUser.getEmail(),
                    gym.getGymName(), gym.getLocation(), gym.getLatitude(), gym.getLongitude());
            newGym.setFacilities(gym.getFacilities());
            newGym.setDailyFee(gym.getDailyFee());
            userService.deleteUser(existingUser.getId()); // Remove the old User entity
            gymService.saveOrUpdateGym(newGym);         // Save the new Gym user
        }

        return ResponseEntity.ok(Map.of("message", "Gym details updated successfully!"));
    }


    @GetMapping("/details")
    public ResponseEntity<?> getGymDetails(@AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        Gym gym = gymService.findGymByEmail(email);
        if (gym == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

            // Return gym-specific details
        Map<String, Object> gymInfo = Map.of(
            "gymName", gym.getGymName(),
            "location", gym.getLocation(),
            "facilities", gym.getFacilities(),
            "dailyFee", gym.getDailyFee(),
            "latitude", gym.getLatitude(),
            "longitude", gym.getLongitude()
        );
        return ResponseEntity.ok(gymInfo);
    }


    @GetMapping("/gyms")
    public List<Gym> getGyms() {
        return gymService.getAllGyms(); // Returns a list of gyms from the database
    }

}
