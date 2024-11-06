package pt.fe.up.fiteverywhere.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.fe.up.fiteverywhere.backend.Entity.User;
import pt.fe.up.fiteverywhere.backend.Service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.isUserExists(user.getUsername(), user.getEmail())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User already exists");
            return ResponseEntity.badRequest().body(response);
        }
        userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());
        if (loggedInUser != null) {
            // Optionally generate a token (JWT) here and return it
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", loggedInUser);  // Or just return a subset like ID, username, roles, etc.
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid credentials");
            return ResponseEntity.badRequest().body(response);
        }
    }

        @GetMapping("/")
        public ResponseEntity<Map<String, String>> root() {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Welcome to the FitEverywhere API");
            return ResponseEntity.ok(response);
        }

        @GetMapping("/login/success")
        public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
        
            String email = principal.getAttribute("email");
            String name = principal.getAttribute("name");
            
            User user = userService.findOrRegisterOAuthUser(name, email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", user); // Return user info
            return ResponseEntity.ok(response);
        }
        
        

        @GetMapping("/error")
        public ResponseEntity<?> handleError() {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Authentication error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
}
