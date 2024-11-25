package pt.fe.up.fiteverywhere.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.service.UserService;
import pt.fe.up.fiteverywhere.backend.service.user.children.ClientService;
import pt.fe.up.fiteverywhere.backend.service.user.children.GymManagerService;
import pt.fe.up.fiteverywhere.backend.service.user.children.PersonalTrainerService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;
    @Autowired
    private GymManagerService gymManagerService;
    @Autowired
    private PersonalTrainerService personalTrainerService;

    @GetMapping("/error")
    public ResponseEntity<?> handleError() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/login/success")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        try {
            String email = principal.getAttribute("email");
            String name = principal.getAttribute("name");
            System.out.println("Authenticated user: " + name);  // Add debug log to check the value


            Optional<User> user = userService.findUserByEmail(email);
            System.out.println("Authenticated user: " + user);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity.ok(Map.of("message", "Login successful", "user", user.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching user info");
        }
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> createNewUser(
            @RequestParam String role,
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }

        switch (role) {
            case "client":
                clientService.save(new Client(principal.getAttribute("name"), principal.getAttribute("email")));
                break;
            case "gym":
                gymManagerService.save(new GymManager(principal.getAttribute("name"), principal.getAttribute("email")));
                break;
            case "pt":
                personalTrainerService.save(new PersonalTrainer(principal.getAttribute("name"), principal.getAttribute("email")));
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid role"));
        }


        return ResponseEntity.ok(Map.of("message", "Created user with role: " + role));
    }


}